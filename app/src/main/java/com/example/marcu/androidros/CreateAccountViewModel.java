package com.example.marcu.androidros;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

public class CreateAccountViewModel extends AndroidViewModel{

    private final List<User> userList;

    private AppDatabase appDatabase;

    public CreateAccountViewModel( Application application) {
        super(application);

        appDatabase =  AppDatabase.getDatabase(this.getApplication());

        userList = appDatabase.userDao().getAllUsers();

    }

    public List<User> getUserList() {
        return userList;
    }

    public void deleteUser(User user) {
        new deleteAsyncTask(appDatabase).execute(user);
    }

    private static class deleteAsyncTask extends AsyncTask<User, Void, Void> {

        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final User... params) {
            db.userDao().delete(params[0]);
            return null;
        }
    }


}
