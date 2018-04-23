package com.example.marcu.androidros.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;


@Dao
public interface EventDao {

    @Query("SELECT * FROM event")
    List<Event> getAllEvents();

    // find event by id
    @Query("SELECT * FROM event WHERE event_id = (:eventID)")
    List<Event> getFromID(int eventID);

    @Query("DELETE FROM event " +
            "WHERE event_id LIKE :eventID")
    int deleteByID(int eventID);

    @Update
    void updateEvent(Event event);

    @Insert(onConflict = IGNORE)
    void insertEvent(Event event);

    @Delete
    void deleteEvent(Event event);
}
