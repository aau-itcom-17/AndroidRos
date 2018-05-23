package com.example.marcu.androidros.Favourites;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
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

public class EditUserPage extends AppCompatActivity {


    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference myDatabaseRef;
    private String profilePictureRef;
    private File profilePicFile;

    //Context context = getApplicationContext();

    public String databaseOldPassword;

    private static int RESULT_LOAD_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_page);


        final TextView editFirstName = (TextView) findViewById(R.id.editFirstName);
        final TextView editLastName = (TextView) findViewById(R.id.editLastName);
        final TextView editEmail = (TextView) findViewById(R.id.editEmail);
        final Button saveButton = (Button) findViewById(R.id.saveButton);
        final TextView editPassword = (TextView) findViewById(R.id.editPassword);
        final TextView repeatEditPassword = (TextView) findViewById(R.id.repeatEditPassword);
        final TextView oldPasswordTextview = (TextView) findViewById(R.id.oldPassword);
        final ImageView profilePicture = (ImageView) findViewById(R.id.profile_picture_view);
        final Button changeProfilePictureButton = (Button) findViewById(R.id.change_profile_picture_button);

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
                User user = dataSnapshot.child(uid).getValue(User.class); // Retrieving the user's information from the database
                editFirstName.setText(user.getFirstName());
                editLastName.setText(user.getLastName());
                editEmail.setText(user.getEmail());
                databaseOldPassword = user.getPassword();

                if (firebaseUser != null) {
                    if (user != null) {
                        profilePictureRef = user.getProfilePicture();
                        Log.i("Firebase", "Profile picture reference: " + profilePictureRef);
                    }
                    if (profilePictureRef != null) {
                        profilePicFile = new File(profilePictureRef);
                        if (profilePicFile.exists()) {
                            profilePicture.setImageBitmap(BitmapFactory.decodeFile(profilePictureRef));
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

                //String firstName = editFirstName.getText().toString().trim();
                //String lastName = editLastName.getText().toString().trim();
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
                            myDatabaseRef.child("users").child(firebaseUser.getUid()).child("firstName").setValue(editFirstName.getText().toString().trim()); // Changing the value from edit profil in database
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
        //permissionToast = Toast.makeText(context,permission,toastDuration);
        // permissionToast.show();

    }

    private boolean checkGalleryPermissions() {
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
