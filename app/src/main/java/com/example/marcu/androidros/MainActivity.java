package com.example.marcu.androidros;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Intent intent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void nextActivityButtonClicked (View view){
        intent.setClass(this,CreateAccountActivity.class);
        startActivity(intent);
        Log.i("Hej", "Going to CreateAccount");

    }
}
