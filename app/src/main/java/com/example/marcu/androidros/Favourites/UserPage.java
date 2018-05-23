package com.example.marcu.androidros.Favourites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserPage extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private ArrayList<Event> events = new ArrayList<>();
    private List<String> eventIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);


        final TextView fName = (TextView) findViewById(R.id.getFirstName);
        final TextView lName = (TextView) findViewById(R.id.getLastName);
        final TextView email = (TextView) findViewById(R.id.getEmail);
        Button editProfile = (Button) findViewById(R.id.edit_profile_button);

        // Access data to firebase
        database = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // Get the user that is logged in
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = firebaseUser.getUid(); //Get the ID of the user that is logged in
                User user = dataSnapshot.child(uid).getValue(User.class); // Retrieving the user's information from the database
                fName.setText(user.getFirstName());
                lName.setText(user.getLastName());
                email.setText(user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(UserPage.this, EditUserPage.class);
                UserPage.this.startActivity(myIntent);
            }
        });





    }
}
