package com.example.pulseoximeter2021.DataLayer.Models.Firebase;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Record implements Serializable {
    private int length;
    private List<Integer> irValues;
    private List<Integer> bpmValues;
    private int oxygen;
    private Double temperature;
    private String message;
    private String dateAndTime;
    private String fullName;

    public Record() {}

    public Record(int length, List<Integer> irValues,
                  List<Integer> bpmValues, int oxygen,
                  Double temperature, String message,
                  String dateAndTime, String fullName) {
        this.length = length;
        this.irValues = irValues;
        this.bpmValues = bpmValues;
        this.oxygen = oxygen;
        this.temperature = temperature;
        this.message = message;
        this.fullName = fullName;
        this.dateAndTime = dateAndTime;
    }

    public int getLength() {
        return length;
    }

    @Exclude
    public int getLenghtAsMilis() {
        return length * 1000;
    }

    public void setLength(int length) {
        this.length = length;
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

    @Exclude
    public String getDateAndTimeAsString() {
        return dateAndTime;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Exclude
    public Date getDateAndTimeAsDate() {
        try {
            return new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(dateAndTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Exclude
    public Date getShortDateAndTime() {
        try {
            return new SimpleDateFormat("yyyy.MM.dd hh:mm:ss").parse(dateAndTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @Exclude
    public static Record GenerateRandom()
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

        return new Record(10, irValues, bpmValues, 100, (double) 25, "", Calendar.getInstance().getTime().toString(), "fullName");
    }

    @Exclude
    public Integer getAverageBpmValue()
    {
        Integer sum = 0;

        if(bpmValues.isEmpty())
            return sum;

        for (Integer bpmValue :
                bpmValues) {
            sum += bpmValue;
        }
        return sum / bpmValues.size();
    }

    @Exclude
    public Integer getMaxBpmValue()
    {
        Integer max = Integer.MIN_VALUE;

        for (Integer bpmValue :
                bpmValues) {
            if(bpmValue > max)
                max = bpmValue;
        }
        return max;
    }

    @Exclude
    public Integer getMinBpmValue()
    {
        Integer min = Integer.MAX_VALUE;

        for (Integer bpmValue :
                bpmValues) {
            if(bpmValue < min)
                min = bpmValue;
        }
        return min;
    }

}
