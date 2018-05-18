package com.example.marcu.androidros.List;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.master.glideimageview.GlideImageView;
import com.squareup.picasso.Picasso;

import com.bumptech.glide.Glide;
import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.R;
import com.squareup.picasso.Picasso;

public class EventInfoActivity extends AppCompatActivity {
    private static final String TAG = "EventInfoActivity";
    private Event event;
    private TextView eventTitle;
    GlideImageView eventImage;
    private TextView eventDate;
    private TextView eventTime;
    private TextView eventDescription;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        eventTitle = (TextView) findViewById(R.id.event_title_view);
        eventImage = (GlideImageView) findViewById(R.id.event_image_view);
        eventDate = (TextView) findViewById(R.id.event_date_view);
        eventTime = (TextView) findViewById(R.id.event_time_view);
        eventDescription = (TextView) findViewById(R.id.event_description_view);



        if (getIntent().getExtras() != null) {
            event = getIntent().getExtras().getParcelable("clickedEvent");
        }

        if (event != null) {
            eventTitle.setText(event.getName());
        }
        if (event != null) {
            eventDate.setText(" " + event.getDate());
        }
        if (event != null) {
            eventTime.setText(" " + event.getTime());
        }
        if (event != null) {
            eventDescription.setText(event.getDescription());
        }
        if (event != null) {
            String photoPath = event.getPhotoPath();
            eventImage.loadImageUrl(photoPath);

        }







    }

}
