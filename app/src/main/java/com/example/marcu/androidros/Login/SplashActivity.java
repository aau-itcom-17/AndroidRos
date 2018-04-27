package com.example.marcu.androidros.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.marcu.androidros.Database.AppDatabase;
import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.Map.MapActivity;
import com.example.marcu.androidros.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    public static AppDatabase appDatabase;
    Context context;
    public static User user;
    List<User> users;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        // decide here whether to navigate to Login or Main Activity
        context = getApplicationContext();
        appDatabase = AppDatabase.getDatabase(context);
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null){
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

/* ROOM IMPLEMENTATION
        if (pref.getBoolean("activity_executed", false)) {
            users = appDatabase.userDao().getAllUsers();
            for (int i = 0; i < users.size(); i++){
                Log.i("USERS", users.get(i).getFirstName());
                if(users.get(i).isLoggedIn()){
                    user = appDatabase.userDao().getFromID(i+1);
                }
            }
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
            finish();

        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        */
    }
}

