package com.example.marcu.androidros.Create;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CreateActivity extends AppCompatActivity {

    private static final String TAG = "CreateEventActivity";

    private Button getLocation;
    private Button finishEvent;
    private Button uploadPhoto;
    private Button takePhoto;

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int CAMERA_REQUEST = 1;
    private Bitmap imageBitmap;
    private String currentPhotoPath;
    private ImageView imageView;

    private TextView locationLatitude;
    private TextView locationLongitude;
    private EditText nameOfEventEdit;
    private EditText eventDescriptionEdit;

    // Date implementation variables
    private TextView dTv;
    private Button dBtn;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    //Time implementation
    private TextView tTv;
    private Button tBtn;

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private LocationTrack locationTracker;

    double longitude, latitude;

    private final static int ALL_PERMISSIONS_RESULT = 101;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        setUpBottomNavigationView();

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        finishEvent = (Button) findViewById(R.id.finish_new_event_creation);
        locationLongitude = (TextView) findViewById(R.id.location_longitude);
        locationLatitude = (TextView) findViewById(R.id.location_latitude);
        nameOfEventEdit = findViewById(R.id.name_input);

        eventDescriptionEdit = findViewById(R.id.enter_event_description);

        this.imageView = (ImageView) this.findViewById(R.id.image_view);

        takePhoto = findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // ensure there is a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }

                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                    }
                }
                startActivity(takePictureIntent);
            }
        });

        getLocation = (Button) findViewById(R.id.get_location);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationTracker = new LocationTrack(CreateActivity.this);

                if(locationTracker.canGetLocation()){

                    longitude = locationTracker.getLongitude();
                    latitude = locationTracker.getLatitude();

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


    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(CreateActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTracker.stopListener();
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() throws IOException{
        // create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /*suffix */
                storageDir /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    // Method handling image scaling
    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    private void setUpBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(CreateActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
    }



}
