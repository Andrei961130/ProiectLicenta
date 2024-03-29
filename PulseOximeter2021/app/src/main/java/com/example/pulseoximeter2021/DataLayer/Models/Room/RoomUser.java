package com.example.pulseoximeter2021.DataLayer.Models.Room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoomUser
{
    @PrimaryKey
    @NonNull
    private String uid;
    private String email;
    private String firstName;
    private String lastName;
    private String birthday;
    private String gender;
    private Boolean isDoctor;
    private String photoUrl;

    public RoomUser(){}

    public RoomUser(String uid, String email,
                    String firstName, String lastName,
                    String birthday, String gender,
                    Boolean isDoctor)
    {
        this.uid = uid;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.gender = gender;
        this.isDoctor = isDoctor;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getDoctor() {
        return isDoctor;
    }

    public void setDoctor(Boolean doctor) {
        isDoctor = doctor;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
