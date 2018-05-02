package com.example.marcu.androidros.Login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.marcu.androidros.Intro.IntroActivity;
import com.example.marcu.androidros.Map.MapActivity;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.Prefs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "AnonymousAuth";

    private static final String EMAIL = "email";

    //private Prefs prefs;

    SharedPreferences prefs = null;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;




    Intent intent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("com.example.marcu.androidros", MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        if (firebaseUser != null){
            startActivity(new Intent(this, MapActivity.class));
            finish();
        }


    }

    public void nextActivityButtonClicked (View view){
        intent.setClass(this, CreateAccountActivity.class);
        startActivity(intent);
        Log.i("TEST", "Going to CreateAccount");

    }
    public void loginActivityButtonClicked (View view){
        intent.setClass(this, LoginWithFirebaseActivity.class);
        startActivity(intent);
        Log.i("TEST", "Going to Login Page");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
           intent.setClass(MainActivity.this, IntroActivity.class);
           startActivity(intent);
           prefs.edit().putBoolean("firstrun", false).apply();
        }
    }
}
