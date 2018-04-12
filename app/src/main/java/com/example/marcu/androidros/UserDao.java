package com.example.marcu.androidros;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {
    // list of all users
    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    // find user by id
    @Query("SELECT * FROM user WHERE user_id = (:userID)")
    List<User> getFromID(int userID);



    // find user by first and last name
    @Query("SELECT * FROM user WHERE first_name LIKE :first AND "
            + "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Insert (onConflict = IGNORE)
    void insert(User user);

    @Insert (onConflict = IGNORE)
    void insertWithFriends(User user, List<User> friends);

    @Delete
    void delete(User user);

    // use db.clearAllTables();
    @Query("DELETE FROM user")
    void nukeTable();

    @Update
    void update(User user);

    @Query("DELETE FROM user " +
            "WHERE user_id LIKE :userID")
    int deleteByID(int userID);

    @Query("DELETE FROM user " + "WHERE email LIKE :email AND " + "password LIKE :pass")
    int deleteByEmailAndPass(String email, String pass);


}