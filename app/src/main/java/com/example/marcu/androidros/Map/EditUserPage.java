package com.example.marcu.androidros.Map;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class EditUserPage extends AppCompatActivity {


    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference myDatabaseRef;

    //Context context = getApplicationContext();






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


                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(),"Password is not matching", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password is too small", Toast.LENGTH_SHORT).show();
                } else if (!CreateAccountActivity.isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Email is not valid", Toast.LENGTH_SHORT).show();
                }
                else {
                    myDatabaseRef.child("users").child(firebaseUser.getUid()).child("firstName").setValue(editFirstName.getText().toString().trim()); // Changing the value from edit profil in database
                    myDatabaseRef.child("users").child(firebaseUser.getUid()).child("lastName").setValue(editLastName.getText().toString().trim());
                    myDatabaseRef.child("users").child(firebaseUser.getUid()).child("email").setValue(editEmail.getText().toString().trim());
                    myDatabaseRef.child("users").child(firebaseUser.getUid()).child("password").setValue(editPassword.getText().toString().trim());
                    Toast.makeText(getApplicationContext(), "User info is saved", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


}
