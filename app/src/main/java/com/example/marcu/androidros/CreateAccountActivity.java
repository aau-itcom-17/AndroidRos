package com.example.marcu.androidros;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CreateAccountActivity extends AppCompatActivity {

    private Intent intent = new Intent();
    private TextView editFirstName;
    private TextView editLastName;
    private TextView editEmail;
    private TextView editPassword;
    private TextView editConfirmPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    public void createAccountButtonClicked (View view){

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

    }
}




      /*
        editFirstName = (EditText) findViewById(R.id.firstNameField); // first name
        firstName = editFirstName.getText().toString();
        editLastName = (EditText) findViewById(R.id.lastNameField); // last name
        lastName = editLastName.getText().toString();
        editEmail= (EditText) findViewById(R.id.emailField); // email
        email = editEmail.getText().toString();
        editPassword = (EditText) findViewById(R.id.passField);// password
        password = editPassword.getText().toString();
        editConfirmPassword = (EditText) findViewById(R.id.confirmPassField); //confirm password
        confirmPassword = editConfirmPassword.getText().toString();
*/
// if password == confirm && if email exists.


