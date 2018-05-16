package com.example.marcu.androidros.List;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.R;

public class EventInfoActivity extends AppCompatActivity {
    private static final String TAG = "EventInfoActivity";
    private Event event;
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        titleView = (TextView) findViewById(R.id.event_info_title_view);

        if (getIntent().getExtras() != null) {
            event = getIntent().getExtras().getParcelable("clickedEvent");
        }

        if (event != null) {
            titleView.setText(event.getName());
        }


    }
}
