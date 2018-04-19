package com.example.marcu.androidros;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MapTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);
        Log.i("TAG", "USER IS LOGGED IN " +  SplashActivity.user.getFirstName() + " " + SplashActivity.user.getLastName());
    }
}
