package com.example.marcu.androidros.ShowEvent;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.marcu.androidros.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventActivity extends Activity {


    private DatabaseReference mDataBase;
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
}
