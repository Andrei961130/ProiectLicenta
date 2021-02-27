package com.example.pulseoximeter2021.DataLayer.Models;

import android.util.Pair;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Record implements Serializable {
    private int lenght;
    private List<Integer> irValues;
    private List<Pair<Integer, Integer>> bpmValues;
    private int oxygen;
    private Double temperature;
    private String message;
    private String dateAndTime;

    public Record() {}

    public Record(int lenght, List<Integer> irValues,
                  List<Pair<Integer, Integer>> bpmValues, int oxygen,
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

    public int getLenght() {
        return lenght;
    }

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

    public List<Pair<Integer, Integer>> getBpmValues() {
        return bpmValues;
    }

    public void setBpmValues(List<Pair<Integer, Integer>> bpmValues) {
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

    public String getDateAndTimeAsString() {
        return dateAndTime;
    }

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
}
