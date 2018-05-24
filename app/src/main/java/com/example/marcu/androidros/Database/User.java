package com.example.marcu.androidros.Database;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.marcu.androidros.Login.MainActivity.favourites;
import static com.example.marcu.androidros.Login.MainActivity.invites;


public class User {

    public User(String firstName, String lastName, String email, String password, String userID, boolean isLoggedIn, String profilePicture){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
        this.profilePicture = profilePicture;
        this.userID = userID;
    }

    public User (){
    }
    public User(String firstName, String lastName, String email, String password, boolean isLoggedIn, String profilePicture){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
        this.profilePicture = profilePicture;
    }


    private String userID;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String location;

    private String profilePicture;

//    @ColumnInfo (name = "friends")
//    private List<User> friends;

    // using ignore to not get error.

    List<Event> events;
    //List<Event> favourites;
    List<Event> invites;


    private boolean isLoggedIn;


    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /*public List<Event> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<Event> favourites) {
        this.favourites = favourites;
    }*/

    public List<Event> getInvites() {
        return invites;
    }

    public void setInvites(List<Event> invites) {
        this.invites = invites;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

}

