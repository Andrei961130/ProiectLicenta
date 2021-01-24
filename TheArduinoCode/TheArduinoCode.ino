/*
  Optical Heart Rate Detection (PBA Algorithm) using the MAX30105 Breakout
  By: Nathan Seidle @ SparkFun Electronics
  Date: October 2nd, 2016
  https://github.com/sparkfun/SparkFun_MAX3010x_Sensor_Library/tree/master/examples
  This is a demo to show the reading of heart rate or beats per minute (BPM) using
  a Penpheral Beat Amplitude (PBA) algorithm.

  It is best to attach the sensor to your finger using a rubber band or other tightening
  device. Humans are generally bad at applying constant pressure to a thing. When you
  press your finger against the sensor it varies enough to cause the blood in your
  finger to flow differently which causes the sensor readings to go wonky.

  Hardware Connections (Breakoutboard to Arduino):
  -5V = 5V (3.3V is allowed) (Wemos/ESP8266: use 3.3v -> VIN)
  -GND = GND
  -SDA = A4 (or SDA) (Wemos/ESP8266: D2)
  -SCL = A5 (or SCL) (Wemos/ESP8266: D1)
  -INT = Not connected

  The MAX30105 Breakout can handle 5V or 3.3V I2C logic. We recommend powering the board with 5V
  but it will also run at 3.3V.
*/


#include "MAX30105.h"
#include "heartRate.h"
#include "ArduinoJson.h"

MAX30105 particleSensor;

const byte RATE_SIZE = 4; //Increase this for more averaging. 4 is good.
byte rates[RATE_SIZE]; //Array of heart rates
byte rateSpot = 0;
long lastBeat = 0; //Time at which the last beat occurred

float bpm;
int avgBpm;

long samplesTaken = 0; //Counter for calculating the Hz or read rate
long unblockedValue; //Average IR at power up
long startTime; //Used to calculate measurement rate

int oxygen; 
int degOffset = 4; //calibrated Farenheit degrees
int irOffset = 1800;
int count;
int noFinger;
//auto calibrate
int avgIr;
int avgTemp;
bool sensedBeat = false;

static const unsigned long REFRESH_INTERVAL = 1000; // ms
static unsigned long lastRefreshTime = 0;

#define INPUT_SIZE 64
#define DELIMITER '\n'      // Message delimiter. It must match with Android class one;

const int LEDBTN_pin = 13;   // Digital output for LED (button)

int messageCode = 0;        // Digital LED value

char input[INPUT_SIZE + 1];
char *ch;
bool endmsg = false;

void setup()
{
   Serial.begin(9600);                         // Begin the serial monitor at 9600bps
  
   ch = &input[0];

  // Initialize sensor
  if (!particleSensor.begin(Wire, I2C_SPEED_FAST)) //Use default I2C port, 400kHz speed
  {
    Serial.println("MAX30105 was not found. Please check wiring/power. ");
    while (1);
  }
  //The LEDs are very low power and won't affect the temp reading much but
  //you may want to turn off the LEDs to avoid any local heating
  particleSensor.setup(0); //Configure sensor. Turn off LEDs
  particleSensor.enableDIETEMPRDY(); //Enable the temp ready interrupt. This is required.
  
  //Setup to sense up to 18 inches, max LED brightness
  byte ledBrightness = 25; //Options: 0=Off to 255=50mA=0xFF hexadecimal. 100=0x64; 50=0x32 25=0x19
  byte sampleAverage = 4; //Options: 1, 2, 4, 8, 16, 32
  byte ledMode = 2; //Options: 1 = Red only, 2 = Red + IR, 3 = Red + IR + Green
  int sampleRate = 400; //Options: 50, 100, 200, 400, 800, 1000, 1600, 3200
  int pulseWidth = 411; //Options: 69, 118, 215, 411
  int adcRange = 2048; //Options: 2048, 4096, 8192, 16384

  particleSensor.setup(ledBrightness, sampleAverage, ledMode, sampleRate, pulseWidth, adcRange); //Configure sensor with these settings
  
  particleSensor.setPulseAmplitudeRed(0x0A); //Turn Red LED to low to indicate sensor is running
  particleSensor.setPulseAmplitudeGreen(0); //Turn off Green LED
  particleSensor.enableDIETEMPRDY(); //Enable the temp ready interrupt. This is required.

//  particleSensor.shutDown();
//  SendSensorStatus("OFF");
}

void SendReadings(bool detectedFinger, long irValue = 0, bool onlyIR = true, float bpm = 0, int avgBpm = 0, int oxygen = 0, float temperature = 0)
{
  DynamicJsonDocument message(48);

  message["detectedFinger"] = detectedFinger;
  message["irValue"] = irValue;
  message["onlyIR"] = onlyIR;
  message["bpm"] = bpm;
  message["avgBpm"] = avgBpm;
  message["oxygen"] = oxygen;
  message["temperature"] = temperature;
  
  serializeJson(message, Serial);
  Serial.println();
}

void SendSensorStatus(String sensorStatus)
{
  DynamicJsonDocument message(24);

  message["sensorStatus"] = sensorStatus;
  
  serializeJson(message, Serial);
  Serial.println();
}

void loop()
{
  delay(2);
  sensedBeat = false;  
  
  if (Serial.available()) // If the bluetooth has received any character
  {
    while (Serial.available() && (!endmsg)) { // until (end of buffer) or (newline)
      *ch = Serial.read();                    // read char from serial
      if (*ch == DELIMITER) {
        endmsg = true;                        // found DELIMITER
        *ch == 0;
      }
      else ++ch;                              // increment index
    }

    if ((endmsg) && (ch != &input[0]))        // end of (non empty) message !!!
    {
        messageCode = atoi(input);
        if (messageCode == 1) 
          {
            particleSensor.wakeUp();
            SendSensorStatus("ON");
          }
        else if(messageCode == 0)
          {
            particleSensor.shutDown();
            SendSensorStatus("OFF");
          }
        
        Serial.write(DELIMITER);
    }
    if (endmsg) {
      endmsg = false;
      *ch = 0;
      ch = &input[0];                         // Return to first index, ready for the new message;
    }
  }

  
  long irValue = particleSensor.getIR();
  delay(2);
  
  if (checkForBeat(irValue) == true)
  {
    //We sensed a beat!
    sensedBeat = true;
    
    long delta = millis() - lastBeat;
    lastBeat = millis();

    bpm = 60 / (delta / 1000.0);

    if (bpm < 200 && bpm > 50)
    {
      rates[rateSpot++] = (byte)bpm; //Store this reading in the array
      rateSpot %= RATE_SIZE; //Wrap variable

      //Take average of readings
      avgBpm = 0;
      for (byte x = 0 ; x < RATE_SIZE ; x++)
        avgBpm += rates[x];
      avgBpm /= RATE_SIZE;

      float temperature = particleSensor.readTemperature();
      temperature = temperature + degOffset;

      oxygen = irValue / irOffset;

      SendReadings(true, irValue, false, bpm, avgBpm, oxygen, temperature);
    }
  }
  else
  {
    if(messageCode == 1)
      if (irValue < 50000)
        SendReadings(false);
      else if(!sensedBeat)
        SendReadings(true, irValue);
  }
}
