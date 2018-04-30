package com.example.marcu.androidros.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

@Entity (tableName = "event", foreignKeys = @ForeignKey(
        entity = User.class,
        childColumns = "event_id",
        parentColumns = "user_id",
        onDelete = ForeignKey.CASCADE)
)
public class Event {

    public Event(String name, String description, String photoPath, String time, String date, Double latitude, Double longitude) {
        this.name = name;
        this.description = description;
        this.photoPath = photoPath;
        this.time = time;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Ignore
    public Event(){
        super();
    }

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "event_id")
    private int eventID;

    @ColumnInfo (name = "name")
    private String name;

    @ColumnInfo (name = "description")
    private String description;

    @ColumnInfo (name = "photo_path")
    private String photoPath;

    @ColumnInfo (name = "time")
    private String time;

    @ColumnInfo (name = "date")
    private String date;

    @ColumnInfo (name = "latitude")
    private Double latitude;

    @ColumnInfo (name = "longitude")
    private Double longitude;

    @ColumnInfo (name = "likes")
    private int likes;

    @ColumnInfo (name = "comments")
    private int comments;

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude){
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
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
