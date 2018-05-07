package com.example.marcu.androidros.Create;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.Map.MapActivity;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.BottomNavigationViewHelper;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateActivity extends AppCompatActivity{

    private static final String TAG = "CreateEventActivity";

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;


    private Bitmap mImageBitmap;
    private ImageButton mPictureButton;

    private Button getLocation;
    private Button finishEvent;

    private String mCurrentPhotoPath;

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
        setContentView(R.layout.activity_create);
        setUpBottomNavigationView();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        finishEvent = (Button) findViewById(R.id.finish_new_event_creation);
        locationLongitude = (TextView) findViewById(R.id.location_longitude);
        locationLatitude = (TextView) findViewById(R.id.location_latitude);
        nameOfEventEdit = findViewById(R.id.name_input);
        eventDescriptionEdit = findViewById(R.id.enter_event_description);

        mProgressDialog = new ProgressDialog(this);

        mStorage = FirebaseStorage.getInstance().getReference();

        mPictureButton = (ImageButton) findViewById(R.id.image_button);
        mPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
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

                Event userEvent = new Event(name, eventDescription, mCurrentPhotoPath, time, date, latitude, longitude, 0, 0);

                mDatabaseRef.child("events").push().setValue(userEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void takePicture(){
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(pictureIntent, CAMERA_REQUEST_CODE);
    }

    /*
    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try{
                photoFile = createImageFile();
            } catch (IOException e){
                // an error occured while creating the image
            }

            if(photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "com.marcu.androidros.fileprovider", photoFile);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        }

    }
    */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PERMISSION);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }


    //private File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
       // File image = File.createTempFile(
              //  imageFileName,  /* prefix */
            //    ".jpg",         /* suffix */
          //      storageDir      /* directory */
        //);

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.getAbsolutePath();
      //  return image;
    //}



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(this.CAMERA_REQUEST_CODE == requestCode && resultCode == RESULT_OK ){

            mProgressDialog.setMessage("Uploading picture...");
            mProgressDialog.show();

            Uri uri = data.getData();

            StorageReference filepath = null;

            filepath = mStorage.child("photos").child(uri.getLastPathSegment());


            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mProgressDialog.dismiss();

                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    Picasso.get().load(downloadUri).fit().centerCrop().into(mPictureButton);

                    Toast.makeText(CreateActivity.this, "Finished task", Toast.LENGTH_SHORT);



                }
            });


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
