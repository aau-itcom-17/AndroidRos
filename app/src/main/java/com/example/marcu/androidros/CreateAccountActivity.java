package com.example.marcu.androidros;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.UUID;

public class CreateAccountActivity extends AppCompatActivity {

    private CreateAccountViewModel viewModel;


    Intent intent = new Intent();
    EditText editFirstName;
    EditText editLastName;
    EditText editEmail;
    EditText editPassword;
    EditText editConfirmPassword;
    String firstName;
    String lastName;
    String email;
    String password;
    String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // getting the viewmodel:
        //viewModel = ViewModelProviders.of(this).get(CreateAccountViewModel.class);
    }

    public void createAccountButtonClicked (View view){
        intent.setClass(this, MainActivity.class);
        editFirstName =  (EditText)findViewById(R.id.firstNameEdit);
        editLastName = (EditText)findViewById(R.id.lastNameEdit);
        editEmail = (EditText)findViewById(R.id.emailEdit);
        editPassword = (EditText)findViewById(R.id.passEdit);
        editConfirmPassword = (EditText)findViewById(R.id.confirmPassEdit);
        firstName = editFirstName.getText().toString();
        lastName = editLastName.getText().toString();
        email = editEmail.getText().toString();
        password =editPassword.getText().toString();
        confirmPassword = editConfirmPassword.getText().toString();


        AppDatabase appDatabase = AppDatabase.getDatabase(getApplicationContext());
        User user = new User(firstName,lastName,email,password);
        appDatabase.userDao().insert(user);
        List<User> users = appDatabase.userDao().getAllUsers();
        for (int i = 0; i < users.size(); i++) {
            Log.i("HEJ", users.get(i).getFirstName() + " " + users.get(i).getUserID());
        }
        appDatabase.close();
        startActivity(intent);
        /*
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "users1")
                .allowMainThreadQueries()   // apparently bad implementation
                .build();

        String uniqueID = UUID.randomUUID().toString();
        User user = new User(firstName,lastName,email,password);
        db.userDao().insert(user);

        List<User> users = db.userDao().getAllUsers();
        for (int i = 0; i < users.size(); i++) {
            Log.i("HEJ", users.get(i).getFirstName() + " " + users.get(i).getUserID());
        }

        Log.i("TEST", db.userDao().findByName(firstName, lastName).getPassword());


        //db.clearAllTables(); or db.userDao().nukeTable(); clears the table
        db.userDao().nukeTable();


        db.close();
        startActivity(intent);
        */


    }


    public void createUser () {
        MainActivity.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(MainActivity.TAG, "createUserWithEmail:success");
                            FirebaseUser user = MainActivity.mAuth.getCurrentUser();
                            MainActivity.updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(MainActivity.TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                             //       Toast.LENGTH_SHORT).show();
                            MainActivity.updateUI(null);
                        }

                        // ...
                    }
                });
    }



}




}



