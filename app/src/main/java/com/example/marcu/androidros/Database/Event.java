package com.example.marcu.androidros.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

public class Event {

    public Event(String eventID, String name, String description, String photoPath, String time, String date, String location, int likes, int comments) {
        this.eventID = eventID;
        this.name = name;
        this.description = description;
        this.photoPath = photoPath;
        this.time = time;
        this.date = date;
        this.location = location;
        this.likes = likes;
        this.comments = comments;
    }

    public Event(){

    }

    private String eventID;

    private String name;

    private String description;

    private String photoPath;

    private String time;

    private String date;

    private String location;

    private int likes;

    private int comments;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
