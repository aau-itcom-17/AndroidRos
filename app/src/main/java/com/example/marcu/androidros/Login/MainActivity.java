package com.example.marcu.androidros.Login;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.marcu.androidros.R;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "AnonymousAuth";

    private static final String EMAIL = "email";

    Intent intent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void nextActivityButtonClicked (View view){
        intent.setClass(this, CreateAccountActivity.class);
        startActivity(intent);
        Log.i("TEST", "Going to CreateAccount");

    }
    public void loginActivityButtonClicked (View view){
        intent.setClass(this,LoginActivity.class);
        startActivity(intent);
        Log.i("TEST", "Going to Login Page");
    }
}
