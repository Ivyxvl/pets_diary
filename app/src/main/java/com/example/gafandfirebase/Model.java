package com.example.gafandfirebase;


//model class is used to set and get the data from the database

import com.google.firebase.Timestamp;

public class Model {
    String title, date, time;
    Timestamp timestamp;

    public Model() {
    }

    public Model(String title, String date, String time) {
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}