package com.example.pulseoximeter2021.DataLayer.Models.Firebase;

public class Link {
    private String doctorUid;
    private String pacientUid;

    public Link (){};

    public String getDoctorUid() {
        return doctorUid;
    }

    public String getPacientUid() {
        return pacientUid;
    }

    public void setDoctorUid(String doctorUid) {
        this.doctorUid = doctorUid;
    }

    public void setPacientUid(String pacientUid) {
        this.pacientUid = pacientUid;
    }
}
