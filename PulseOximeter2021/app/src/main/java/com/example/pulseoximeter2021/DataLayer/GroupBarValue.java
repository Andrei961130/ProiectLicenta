package com.example.pulseoximeter2021.DataLayer;

public class GroupBarValue {

    private Integer green;
    private Integer orange;
    private Integer red;

    public GroupBarValue() {
        this.green = 0;
        this.orange = 0;
        this.red = 0;
    }

    public GroupBarValue(Integer green, Integer orange, Integer red) {
        this.green = green;
        this.orange = orange;
        this.red = red;
    }

    public Integer getGreen() {
        return green;
    }

    public void setGreen(Integer green) {
        this.green = green;
    }

    public Integer getOrange() {
        return orange;
    }

    public void setOrange(Integer orange) {
        this.orange = orange;
    }

    public Integer getRed() {
        return red;
    }

    public void setRed(Integer red) {
        this.red = red;
    }

    public void incrementGreen(){
        this.green += 1;
    }
    public void incrementOrange(){
        this.orange += 1;
    }
    public void incrementRed(){
        this.red += 1;
    }
}
