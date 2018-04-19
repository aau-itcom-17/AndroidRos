package com.example.marcu.androidros;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
// when changing underlying structure of database: increment version with +1. (See migration)
// this way users won't loose their data if we change database.
@Database(entities = {User.class, Event.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "application_db";
    private static AppDatabase INSTANCE;


    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DB_NAME)
                    .allowMainThreadQueries() // Bad implementation, FIX (Ask sokol)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }


    public abstract UserDao userDao();

    public abstract EventDao eventDao();

}

