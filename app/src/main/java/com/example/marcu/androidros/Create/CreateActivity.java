package com.example.marcu.androidros.Create;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.Favourites.FavouriteActivity;
import com.example.marcu.androidros.Map.MapActivity;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.BottomNavigationViewHelper;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateActivity extends AppCompatActivity{

    private static final String TAG = "CreateEventActivity";

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mDatabase;
    private StorageReference mStorage;
    private DatabaseReference mDatabaseRef;
    private Uri mImageUri;
    private String mCurrentPhotoPath;
    private String downloadUrl;


    Button mSubmitBtn;
    Button getLocation;
    Button finishEvent;
    ImageButton imageButton;
    ProgressBar progressBar;
    ProgressBar progressBar1;

    private ImageView imageView;

    public TextView locationLatitude;
    public TextView locationLongitude;
    private EditText nameOfEventEdit;
    private EditText eventDescriptionEdit;

    private String name;
    private String eventDescription;
    private String date;
    private String time;
    private String uniqueKey;

    // Date implementation variables
    private TextView dTv;
    private Button dBtn;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    //Time implementation
    private TextView tTv;
    private Button tBtn;

    private ProgressDialog mProgressDialog;

    public LocationTrack locationTracker;
    public LatLng loc;

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION = 1;

    public double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_create_pretty);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference("events");

        finishEvent = (Button) findViewById(R.id.create_event_button);
        nameOfEventEdit = findViewById(R.id.event_title_edit);
        eventDescriptionEdit = findViewById(R.id.event_description_edit);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.event_image_view);
        progressBar.setVisibility(View.GONE);
        progressBar1.setVisibility(View.GONE);

        mProgressDialog = new ProgressDialog(this);

        // Makes the button open the camera
        imageButton = (ImageButton) findViewById(R.id.take_photo_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // ensure there is a camera activity to handle the intent
                    if(ContextCompat.checkSelfPermission(CreateActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CreateActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                    }else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            // Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File...
                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                mImageUri = FileProvider.getUriForFile(CreateActivity.this,
                                        "com.example.android.fileprovider", photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                            }
                        }
                    }
            }
        });

        // Date implementation
        dTv = (TextView) findViewById(R.id.date_text_view);
        dBtn = (Button) findViewById(R.id.date_button);
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
        tTv = (TextView) findViewById(R.id.time_text_view);
        tBtn = (Button) findViewById(R.id.time_button);
        tBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int tMinute = calendar.get(Calendar.MINUTE);
                int tHour = calendar.get(Calendar.HOUR_OF_DAY);

                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTimeSet(TimePicker view, int hourSelected, int minuteSelected) {
                        tTv.setText(String.format("%02d:%02d", hourSelected, minuteSelected));
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

                getCurrentLocation();

                // Creation of event and pushing it to the database
                Event userEvent = new Event(name, eventDescription, downloadUrl, time, date, latitude, longitude, 0, 0);
                mDatabaseRef.child("events").push().setValue(userEvent, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                            DatabaseReference databaseReference) {
                        // set the event id to the key that has been generated
                        uniqueKey = databaseReference.getKey();
                        mDatabaseRef.child("events").child(uniqueKey).child("eventID").setValue(uniqueKey);
                        mDatabaseRef.child("events").child(uniqueKey).child("eventOwner").setValue(FirebaseAuth.getInstance().getUid());
                        uploadFile();

                        // should clear all fields in event and show the sample of the event...
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            mImageUri = Uri.fromFile(new File (mCurrentPhotoPath));
            imageView.setImageURI(mImageUri);
            //mImageUri = data.getData();
            Log.i(TAG, "Permission granted... imageURI: " + mImageUri);
        }else {
            Log.i(TAG, "Permission not granted");
            Toast.makeText(CreateActivity.this,"Permission not granted", Toast.LENGTH_SHORT).show();

        }
    }

    // getting the uri from the image taken..
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile(){
        if(mImageUri != null){
            // creating a storage name for the file.
            // Taking current time in milliseconds to get a unique filename
            StorageReference fileReference = mStorage.child(System.currentTimeMillis()
            + "." +  mImageUri.getPath().substring(mImageUri.getPath().lastIndexOf(".")+1));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Using a delayed thread so the user sees the event is created before it turns to 0 again.
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }, 2000);
                            progressBar1.setVisibility(View.GONE);
                            Toast.makeText(CreateActivity.this, "Event was successfully created", Toast.LENGTH_SHORT).show();
                            downloadUrl = taskSnapshot.getDownloadUrl().toString();
                            mDatabaseRef.child("events").child(uniqueKey).child("photoPath").setValue(downloadUrl);
                            startActivity(new Intent(CreateActivity.this, FavouriteActivity.class));

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar1.setVisibility(View.VISIBLE);
                            progressBar.setProgress((int)(progress));
                            //ProgressDialog is a bad user experience since the user cannot use the app while their event is uploaded...
                        }
                    });
        }else{
            Toast.makeText(this, "No picture selected", Toast.LENGTH_SHORT).show();
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public void getCurrentLocation(){
        locationTracker = new LocationTrack(CreateActivity.this);
        if(locationTracker.canGetLocation()){
            longitude = locationTracker.getLongitude();
            latitude = locationTracker.getLatitude();
            loc = new LatLng(latitude, longitude);
        } else {
            locationTracker.showSettingsAlert();
        }
        locationTracker.stopListener();
    }

}
