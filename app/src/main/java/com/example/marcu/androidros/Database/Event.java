package com.example.marcu.androidros.Database;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class Event {

    public Event(String name, String description, String photoPath, String time, String date, double latitude, double longitude, int likes, int comments) {
        this.name = name;
        this.description = description;
        this.photoPath = photoPath;
        this.time = time;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Event(String s, String toString, String currentPhotoPath, String string, String s1, double latitude, double longitude){

    }

    private String eventID;

    private String name;

    private String description;

    private String photoPath;

    private String time;

    private String date;

    private Double latitude;

    private Double longitude;

    private int likes;

    private int comments;

    public  String getEventID(){
        return eventID;
    }

    public void setEventID(String eventID){
        this.eventID = eventID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}
