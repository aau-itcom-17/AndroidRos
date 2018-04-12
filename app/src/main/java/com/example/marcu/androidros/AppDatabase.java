package com.example.marcu.androidros;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
//    private static AppDatabase INSTANCE;
    public abstract UserDao userDao();

}

