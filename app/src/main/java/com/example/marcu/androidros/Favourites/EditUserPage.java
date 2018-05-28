package com.example.marcu.androidros.Favourites;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.Login.CreateAccountActivity;
import com.example.marcu.androidros.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;

import static com.example.marcu.androidros.Login.CreateAccountActivity.rotateBitmap;

public class EditUserPage extends AppCompatActivity {

    String picturePath = null;
    private TextView editFirstName;
    private TextView editLastName;
    private TextView editEmail;
    private TextView editPassword;
    private TextView repeatEditPassword;
    private TextView oldPasswordTextview;
    private Button saveButton;
    private Button changeProfilePictureButton;
    private ImageView profilePicture;
    private Bitmap loadedBitmap;
    User user;


    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference myDatabaseRef;
    private String profilePictureRef;
    private File profilePicFile;
    public String databaseOldPassword;
    private static int RESULT_LOAD_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_page);

        editFirstName = (TextView) findViewById(R.id.editFirstName);
        editLastName = (TextView) findViewById(R.id.editLastName);
        editEmail = (TextView) findViewById(R.id.editEmail);
        saveButton = (Button) findViewById(R.id.saveButton);
        editPassword = (TextView) findViewById(R.id.editPassword);
        repeatEditPassword = (TextView) findViewById(R.id.repeatEditPassword);
        oldPasswordTextview = (TextView) findViewById(R.id.oldPassword);
        profilePicture = (ImageView) findViewById(R.id.profile_picture_view);
        changeProfilePictureButton = (Button) findViewById(R.id.change_profile_picture_button);

        // Firebase
        FirebaseAuth.AuthStateListener mAuthListener;


        // Access data from firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // Get the user that is logged in
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = firebaseUser.getUid(); //Get the ID of the user that is logged in
                user = dataSnapshot.child(uid).getValue(User.class); // Retrieving the user's information from the database
                if (firebaseUser != null) {
                    if (user != null) {
                        editFirstName.setText(user.getFirstName());
                        editLastName.setText(user.getLastName());
                        editEmail.setText(user.getEmail());
                        databaseOldPassword = user.getPassword();
                        profilePictureRef = user.getProfilePicture();
                        Log.i("Firebase", "Profile picture reference: " + profilePictureRef);
                    }
                    if (profilePictureRef != null) {
                        profilePicFile = new File(profilePictureRef);
                        Log.i("PROFILE PIC FILE", profilePicFile.toString());
                        if (profilePicFile.exists()) {
                            profilePicture.setImageBitmap(BitmapFactory.decodeFile(profilePictureRef));
                            Log.i("PROFILE PIC FILE", profilePicFile.toString());

                        } else {
                            Toast.makeText(EditUserPage.this, "Couldn't load profile picture... " +
                                    "please change your profile picture.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                String confirmPassword = repeatEditPassword.getText().toString().trim();
                String oldPassword = oldPasswordTextview.getText().toString().trim();


                if (!password.isEmpty() || !confirmPassword.isEmpty() || !oldPassword.isEmpty()) {

                    // If old password
                    if (oldPassword.equals(databaseOldPassword)) {

                        if (password.length() < 6) {
                            Toast.makeText(getApplicationContext(), "The new password is too small", Toast.LENGTH_SHORT).show();
                        } else if (!password.equals(confirmPassword)) {
                            Toast.makeText(getApplicationContext(), "The password is not matching", Toast.LENGTH_SHORT).show();
                        } else if (!CreateAccountActivity.isValidEmail(email)) {
                            Toast.makeText(getApplicationContext(), "The email is not valid", Toast.LENGTH_SHORT).show();
                        } else {
                            myDatabaseRef.child("users").child(firebaseUser.getUid()).child("firstName").setValue(editFirstName.getText().toString().trim()); // Changing the value from edit profile in database
                            myDatabaseRef.child("users").child(firebaseUser.getUid()).child("lastName").setValue(editLastName.getText().toString().trim());
                            myDatabaseRef.child("users").child(firebaseUser.getUid()).child("email").setValue(editEmail.getText().toString().trim());
                            myDatabaseRef.child("users").child(firebaseUser.getUid()).child("password").setValue(editPassword.getText().toString().trim());
                            Toast.makeText(getApplicationContext(), "User info is saved", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "The old password is wrong", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    myDatabaseRef.child("users").child(firebaseUser.getUid()).child("firstName").setValue(editFirstName.getText().toString().trim()); // Changing the value from edit profil in database
                    myDatabaseRef.child("users").child(firebaseUser.getUid()).child("lastName").setValue(editLastName.getText().toString().trim());
                    myDatabaseRef.child("users").child(firebaseUser.getUid()).child("email").setValue(editEmail.getText().toString().trim());
                    //user.setProfilePicture(profilePictureRef);
                    myDatabaseRef.child("users").child(firebaseUser.getUid()).child("profilePicture").setValue(picturePath);
                    Log.i("PROFILE PICTURE REF", "REF "+ profilePictureRef);
                    Toast.makeText(getApplicationContext(), "User info is saved", Toast.LENGTH_LONG).show();

                }


            }
        });


    }

    public void addProfilePictureButtonClicked(View view) {
        if (checkGalleryPermissions()) {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            Log.i("PICTURE PATH", picturePath.toString());

            loadedBitmap = BitmapFactory.decodeFile(picturePath);
            Log.i("LOADED BITMAP", loadedBitmap.toString());

            // Checking rotation of image and turning it the right way
            ExifInterface exif = null;
            try {
                File pictureFile = new File(picturePath);
                exif = new ExifInterface(pictureFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ExifInterface.ORIENTATION_NORMAL;

            if (exif != null) {
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            }

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    loadedBitmap = rotateBitmap(loadedBitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    loadedBitmap = rotateBitmap(loadedBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    loadedBitmap = rotateBitmap(loadedBitmap, 270);
                    break;
            }
            //

            profilePicture.setImageBitmap(loadedBitmap);
            user.setProfilePicture(loadedBitmap.toString());
            //uploadPhotoView.setText(nothing);
            Log.i("PROFILE PICTURE", profilePicture.toString());
        }
    }
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private boolean checkGalleryPermissions(){
        // Here, this is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                return false;
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        RESULT_LOAD_IMAGE);
                return true;
            }
        } else {
            // Permission has already been granted
            return true;
        }
    }

}
