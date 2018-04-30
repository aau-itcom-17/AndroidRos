package com.example.marcu.androidros.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.Map.MapActivity;
import com.example.marcu.androidros.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

//    public static AppDatabase appDatabase;
//    Context context;
//    public static User user;
//    List<User> users;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    private DatabaseReference myDatabaseRef;
    private FirebaseDatabase database;
    String TAG = "Firebase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference();

    }
    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (auth.getCurrentUser() != null) {
            onAuthSuccess(auth.getCurrentUser());
            myDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user  = dataSnapshot.getValue(User.class);
                    Log.i(TAG, "User " + user.getFirstName() + " " + user.getLastName() + " " + user.getEmail());
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w(TAG, "Failed to read value.", error.toException());

                }
            });
        }else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
    private void onAuthSuccess(FirebaseUser user) {

        // Go to MainActivity
        startActivity(new Intent(this, MapActivity.class));
        finish();
    }



}

