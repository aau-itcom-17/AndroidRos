package com.example.marcu.androidros;

import android.arch.lifecycle.LiveData;

import java.util.List;

public class UserRepository {

    private final UserDao userDao;

    public UserRepository(UserDao userDao){
        this.userDao = userDao;
    }

    public List<User> getAllUsers(){
        return userDao.getAllUsers();
    }

    public User getUserById(int userId){
        return userDao.getFromID(userId);
    }
    public User findByName(String firsName, String lastName){
        return userDao.findByName(firsName,lastName);
    }

    public void deleteUser(User user){
        userDao.delete(user);
    }

    public void addUser(User user){
        userDao.insert(user);
    }

    public void updateUser(User user){
        userDao.update(user);
    }
    public void deleteUserById(int id){
        userDao.deleteByID(id);
    }

    public void deleteUserByEmailAndPass(String email, String pass){
        userDao.deleteByEmailAndPass(email, pass);
    }

    public void nukeTable(){
        userDao.nukeTable();
    }




}
