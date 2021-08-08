package com.example.pulseoximeter2021.Bluetooth;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ArduinoMessage {

    private Boolean detectedFinger;
    private Integer irValue;
    private Boolean onlyIR;
    private Double bpm;
    private Integer avgBpm;
    private Integer oxygen;
    private Double temperature;
    private String sensorStatus;

    public Boolean getDetectedFinger() {
        return detectedFinger;
    }

    public void setDetectedFinger(Boolean detectedFinger) {
        this.detectedFinger = detectedFinger;
    }

    public Integer getIrValue() {
        return irValue;
    }

    public void setIrValue(Integer irValue) {
        this.irValue = irValue;
    }

    public Boolean getOnlyIR() {
        return onlyIR;
    }

    public void setOnlyIR(Boolean onlyIR) {
        this.onlyIR = onlyIR;
    }

    public Double getBpm() {
        return bpm;
    }

    public void setBpm(Double bpm) {
        this.bpm = bpm;
    }

    public Integer getAvgBpm() {
        return avgBpm;
    }

    public void setAvgBpm(Integer avgBpm) {
        this.avgBpm = avgBpm;
    }

    public Integer getOxygen() {
        return oxygen;
    }

    public void setOxygen(Integer oxygen) {
        this.oxygen = oxygen;
    }

    public Boolean getIsSensorStatusON() {
        return sensorStatus.equals("ON");
    }

    public void setSensorStatus(String sensorStatus) {
        this.sensorStatus = sensorStatus;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public static ArduinoMessage parseFromArduino(String receivedMessage)
    {
        ArduinoMessage arduinoMessage = new ArduinoMessage();
        List<String> tokenizedMessage = new ArrayList<String>();
        StringTokenizer stringTokenizer = new StringTokenizer(receivedMessage);

        while(stringTokenizer.hasMoreTokens())
            tokenizedMessage.add(stringTokenizer.nextToken());

        if(tokenizedMessage.isEmpty())
            return null;

        if(tokenizedMessage.size() == 2)
        {
            if (tokenizedMessage.get(0).equals("sensorStatus")) {
                arduinoMessage.sensorStatus = tokenizedMessage.get(1);
                arduinoMessage.detectedFinger = false;
                return arduinoMessage;
            } else if (tokenizedMessage.get(0).equals("detectedFinger")) {
                arduinoMessage.detectedFinger = false;
                return arduinoMessage;
            }
        }

        if(tokenizedMessage.size() == 3)
        {
            arduinoMessage.detectedFinger = true;
            arduinoMessage.irValue = Integer.parseInt(tokenizedMessage.get(1));
            arduinoMessage.onlyIR = true;
            return arduinoMessage;
        }

        arduinoMessage.detectedFinger = true;
        arduinoMessage.irValue = Integer.parseInt(tokenizedMessage.get(1));
        arduinoMessage.onlyIR = false;
        arduinoMessage.bpm = Double.parseDouble(tokenizedMessage.get(3));
        arduinoMessage.avgBpm = Integer.parseInt(tokenizedMessage.get(4));
        arduinoMessage.oxygen = Integer.parseInt(tokenizedMessage.get(5));
        arduinoMessage.temperature = Double.parseDouble(tokenizedMessage.get(6));

        return arduinoMessage;
    }
}