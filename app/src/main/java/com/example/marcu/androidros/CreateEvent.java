package com.example.marcu.androidros;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateEvent extends AppCompatActivity {

    private Button finishEvent;
    private Button uploadPhoto;
    private Button takePhoto;

    private EditText nameOfEvent;
    private EditText eventDescription;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        finishEvent = findViewById(R.id.finish_new_event_creation);
        finishEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        uploadPhoto = findViewById(R.id.upload_photo);
        uploadPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                }
            });

        takePhoto = findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        nameOfEvent = findViewById(R.id.name_input);

        eventDescription = findViewById(R.id.enter_event_description);

    }


}
