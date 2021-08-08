package com.example.pulseoximeter2021.DataLayer.Models.Room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.pulseoximeter2021.DataLayer.Room.MyTypeConverters;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Entity
public class RoomRecord implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;
    private int lenght;
    @TypeConverters(MyTypeConverters.class)
    private List<Integer> irValues;
    @TypeConverters(MyTypeConverters.class)
    private List<Integer> bpmValues;
    private int oxygen;
    private Double temperature;
    private String message;
    private String dateAndTime;

    public RoomRecord() {}

    public RoomRecord(int lenght, List<Integer> irValues,
                      List<Integer> bpmValues, int oxygen,
                      Double temperature, String message,
                      Date dateAndTime) {
        this.lenght = lenght;
        this.irValues = irValues;
        this.bpmValues = bpmValues;
        this.oxygen = oxygen;
        this.temperature = temperature;
        this.message = message;
        setDateAndTime(dateAndTime);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLenght() {
        return lenght;
    }

    @Exclude
    public int getLenghtAsMilis() {
        return lenght * 1000;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public List<Integer> getIrValues() {
        return irValues;
    }

    public void setIrValues(List<Integer> irValues) {
        this.irValues = irValues;
    }

    public List<Integer> getBpmValues() {
        return bpmValues;
    }

    public void setBpmValues(List<Integer> bpmValues) {
        this.bpmValues = bpmValues;
    }

    public int getOxygen() {
        return oxygen;
    }

    public void setOxygen(int oxygen) {
        this.oxygen = oxygen;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    @Exclude
    public Date getDateAndTimeAsDate() {
        try {
            return new SimpleDateFormat("yyyy.MM.dd hh:mm:ss").parse(dateAndTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setDateAndTime(Date dateAndTime) {
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        this.dateAndTime = dateFormat.format(dateAndTime);
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public static RoomRecord GenerateRandom()
    {
        List<Integer> irValues = new ArrayList<>();
        List<Integer> bpmValues = new ArrayList<>();

        Random random = new Random();

        for (int index = 0; index < 300; index++) {

            irValues.add(random.nextInt());
        }

        for (int index = 0; index < 5; index++) {

            bpmValues.add(random.nextInt());
        }

        return new RoomRecord(10, irValues, bpmValues, 100, (double) 25, "", Calendar.getInstance().getTime());
    }

}
