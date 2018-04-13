package com.example.marcu.androidros;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class UserEvents {

    //@Embedded allows nested fields to be referenced directly in the SQL queries
    @Embedded
    private User user;

    //@Relation describes relations between two columns. We have to specify the parent column name, entity column name and entity class.
    @Relation(parentColumn = "id", entityColumn = "userId", entity = Event.class)
    private List<Event> events;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}


