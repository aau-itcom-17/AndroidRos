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

    private Intent intent = new Intent();
    private EditText editFirstName = (EditText)findViewById(R.id.firstNameEdit);
    private EditText editLastName = (EditText)findViewById(R.id.lastNameEdit);
    private EditText editEmail = (EditText)findViewById(R.id.emailEdit);
    private EditText editPassword = (EditText)findViewById(R.id.passEdit);
    private EditText editConfirmPassword = (EditText)findViewById(R.id.confirmPassEdit);
    private String firstName = editFirstName.getText().toString();
    private String lastName = editLastName.getText().toString();
    private String email = editEmail.getText().toString();
    private String password =editPassword.getText().toString();
    private String confirmPassword = editConfirmPassword.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    public void createAccountButtonClicked (View view){
        intent.setClass(this, Test.class);
      //  User user = new User();
        //user.setFirstName(firstName);
        //user.setLastName(lastName);
        //user.setEmail(email);
       // user.setPassword(password);
       // user.setUserID(1);

       // AppDatabase db = Room.databaseBuilder(getApplicationContext(),
        //        AppDatabase.class, "database-name").build();

       // Log.i("TEST", String.valueOf(db.userDao().getAll()));

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


