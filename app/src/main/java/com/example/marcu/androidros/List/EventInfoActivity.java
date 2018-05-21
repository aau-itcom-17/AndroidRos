package com.example.marcu.androidros.List;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marcu.androidros.Map.MapActivity;
import com.google.android.gms.maps.model.LatLng;
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
    private Button showOnMapButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        eventTitle = (TextView) findViewById(R.id.event_title_view);
        eventImage = (GlideImageView) findViewById(R.id.event_image_view);
        eventDate = (TextView) findViewById(R.id.event_date_view);
        eventTime = (TextView) findViewById(R.id.event_time_view);
        eventDescription = (TextView) findViewById(R.id.event_description_view);
        showOnMapButton = (Button) findViewById(R.id.event_showonmap_button);

        showOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EventInfoActivity.this, MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("event_location", new LatLng(event.getLatitude(), event.getLongitude()));
                intent.putExtras(bundle);
                startActivity(intent);

                /*LatLng eventLoc = new LatLng(event.getLatitude(), event.getLongitude());

                Intent intent = new Intent(EventInfoActivity.this, MapActivity.class);
                EventInfoActivity.this.startActivity(intent);
                intent.putExtra("event_location", eventLoc);

                Log.i(TAG, "heyheyheyhyeheyeheyeheyh" + eventLoc);*/

            }
        });

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
