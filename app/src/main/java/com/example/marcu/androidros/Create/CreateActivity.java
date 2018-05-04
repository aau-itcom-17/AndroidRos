package com.example.marcu.androidros.Create;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.
  .Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.Map.MapActivity;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.BottomNavigationViewHelper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CreateActivity extends AppCompatActivity{

    private static final String TAG = "CreateEventActivity";

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    private Button mSubmitBtn;
    private Button getLocation;
    private Button finishEvent;
    private ImageButton imageButton;

    private String currentPhotoPath;
    private ImageView imageView;

    private TextView locationLatitude;
    private TextView locationLongitude;
    private EditText nameOfEventEdit;
    private EditText eventDescriptionEdit;

    private String name;
    private String eventDescription;
    private String date;
    private String time;

    private Uri mImageUri = null;

    private StorageReference mStorage;

    // Date implementation variables
    private TextView dTv;
    private Button dBtn;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    //Time implementation
    private TextView tTv;
    private Button tBtn;

    private ProgressDialog mProgressDialog;

    private LocationTrack locationTracker;
    private LatLng loc;

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION = 1;

    double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_create);
        setUpBottomNavigationView();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        finishEvent = (Button) findViewById(R.id.finish_new_event_creation);
        locationLongitude = (TextView) findViewById(R.id.location_longitude);
        locationLatitude = (TextView) findViewById(R.id.location_latitude);
        nameOfEventEdit = findViewById(R.id.name_input);
        eventDescriptionEdit = findViewById(R.id.enter_event_description);

        imageView = (ImageView) this.findViewById(R.id.image_view);

        mProgressDialog = new ProgressDialog(this);

        mStorage = FirebaseStorage.getInstance().getReference().child("event_pictures");

        // Makes the button open the camera
        imageButton = findViewById(R.id.image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // ensure there is a camera activity to handle the intent
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);} catch (SecurityException e){
                    if(ContextCompat.checkSelfPermission(CreateActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CreateActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                    }
                }
            }
        });

        mSubmitBtn = (Button) findViewById(R.id.submit_image_button);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });



        //Gets the location of the user
        getLocation = (Button) findViewById(R.id.get_location);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationTracker = new LocationTrack(CreateActivity.this);

                if(locationTracker.canGetLocation()){

                    longitude = locationTracker.getLongitude();
                    latitude = locationTracker.getLatitude();

                    loc = new LatLng(latitude, longitude);

                    locationLongitude.setText(Double.toString(longitude));
                    locationLatitude.setText(Double.toString(latitude));

                    Toast.makeText(getApplicationContext(), "Longitude: " + Double.toString(longitude) + "\nLatitude: " + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                } else {
                    locationTracker.showSettingsAlert();
                }

                locationTracker.stopListener();

            }
        });

        // Date implementation
        dTv = (TextView) findViewById(R.id.date);
        dBtn = (Button) findViewById(R.id.choose_date);

        dBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);


                datePickerDialog = new DatePickerDialog(CreateActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                        dTv.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                    }


                }, day, month, year);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); //Disable the possibility to choose earlier dates


                datePickerDialog.show();

            }
        });

// Implementing time
        tTv = (TextView) findViewById(R.id.time);
        tBtn = (Button) findViewById(R.id.choose_time);

        tBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int tMinute = calendar.get(Calendar.MINUTE);
                int tHour = calendar.get(Calendar.HOUR_OF_DAY);

                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourSelected, int minuteSelected) {
                        tTv.setText(hourSelected + ":" + minuteSelected);
                    }
                }, tHour, tMinute, true);
                timePickerDialog.show();


            }
        });

        finishEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name = nameOfEventEdit.getText().toString();
                eventDescription = eventDescriptionEdit.getText().toString();



                date = dTv.getText().toString();
                time = tTv.getText().toString();

                Event userEvent = new Event(name, eventDescription, currentPhotoPath, time, date, latitude, longitude, 0, 0);

                HashMap<String, Object> eventMap = new HashMap<String, Object>();

                eventMap.put("Name", name);
                eventMap.put("user_event", userEvent);

                mDatabaseRef.child("events").push().setValue(eventMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(CreateActivity.this, "Event saved", Toast.LENGTH_SHORT).show();
                        } else{

                            Toast.makeText(CreateActivity.this, "An error ocurred, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");

    }

    // Adds the picture as a file
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void startPosting() {
        mProgressDialog.setMessage("Posting to database...");

        final String title_val = nameOfEventEdit.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && mImageUri != null){

            mProgressDialog.show();

            StorageReference filepath = mStorage.child("event_images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newEvent = mDatabaseRef.push();
                    newEvent.child("event name").setValue(title_val);
                    newEvent.child("image").setValue(downloadUrl.toString());
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            mImageUri = data.getData();
        }
    }


    // Method handling the buttom navigation view
    private void setUpBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(CreateActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
    }



}
