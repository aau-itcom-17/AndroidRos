package com.example.marcu.androidros.Database;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Event implements Parcelable {

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

    public Event(){

    }

    private String eventID;

    private String eventOwner;

    private String name;

    private String description;

    private String photoPath;

    private String time;

    private String date;

    private Double latitude;

    private Double longitude;

    private int likes;

    private int comments;

    private int distance;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
    private String distanceString;

    public String getDistanceString() {
        return distanceString;
    }

    public void setDistanceString(String distanceString) {
        this.distanceString = distanceString;
    }

    protected Event(Parcel in) {
        eventID = in.readString();
        name = in.readString();
        description = in.readString();
        photoPath = in.readString();
        time = in.readString();
        date = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        likes = in.readInt();
        comments = in.readInt();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public  String getEventID(){
        return eventID;
    }

    public void setEventID(String eventID){
        this.eventID = eventID;
    }

    public String getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(String eventOwner) {
        this.eventOwner = eventOwner;
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

    @Override
    public int describeContents() {
        return 0;
    }

    // automated code
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventID);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(photoPath);
        dest.writeString(time);
        dest.writeString(date);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeInt(likes);
        dest.writeInt(comments);
    }
}
