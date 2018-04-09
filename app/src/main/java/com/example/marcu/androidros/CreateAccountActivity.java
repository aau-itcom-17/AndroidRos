package com.example.marcu.androidros;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CreateAccountActivity extends AppCompatActivity {

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
    }

    public void createAccountButtonClicked (View view){
        intent.setClass(this, Test.class);
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

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserID(1);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

        Log.i("TEST", user.getPassword());

        startActivity(intent);

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


