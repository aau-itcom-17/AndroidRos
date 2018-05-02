package com.example.marcu.androidros.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marcu.androidros.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventPopUp extends Activity implements View.OnClickListener{

    private DatabaseReference mDataBase;
    private ImageView mCloseButton;
    private TextView mTitleView;
    private TextView mTimeView;
    private TextView mLocationView;
    private TextView mDescriptionView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_event_popup);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mTitleView = (TextView) findViewById(R.id.title_view);
        mTimeView = (TextView) findViewById(R.id.time_view);
        mLocationView = (TextView) findViewById(R.id.location_view);
        mDescriptionView = (TextView) findViewById(R.id.description_view);
        mCloseButton = (ImageView) findViewById(R.id.close_button);
        mCloseButton.setOnClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*.9), (int) (height*.9));

        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String title = dataSnapshot.child("event1").child("title").getValue().toString();
                String time = dataSnapshot.child("event1").child("time").getValue().toString();
                String location = dataSnapshot.child("event1").child("location").getValue().toString();
                String description = dataSnapshot.child("event1").child("description").getValue().toString();

                mTitleView.setText(title);
                mTimeView.setText(time);
                mLocationView.setText(location);
                mDescriptionView.setText(description);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_button:
                finish();
                break;
        }
    }
}
