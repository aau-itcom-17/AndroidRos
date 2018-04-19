package com.example.marcu.androidros;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Intent intent = new Intent();
    EditText emailEdit;
    EditText passEdit;
    String email;
    String pass;
    Context context;
    AppDatabase appDatabase;
    User user;
    User user1;
    Toast wrongEmailToast;
    Toast wrongPassToast;
    int toastDuration = Toast.LENGTH_LONG;
    CharSequence wrongEmail = "The Email does not exist.";
    CharSequence wrongPass = "The entered password is not correct.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CreateAccountActivity.setHideKeyboardOnTouch(this, findViewById(R.id.activity_login));

        emailEdit = findViewById(R.id.emailEdit1);
        passEdit = findViewById(R.id.passEdit1);
        context = getApplicationContext();
        appDatabase = AppDatabase.getDatabase(context);

    }

    public void loginButton (View view){
        intent.setClass(this,MainActivity.class);
        wrongEmailToast = Toast.makeText(context, wrongEmail, toastDuration);
        wrongPassToast = Toast.makeText(context, wrongPass,toastDuration);
        email = emailEdit.getText().toString();
        pass = passEdit.getText().toString();

        user = appDatabase.userDao().getFromEmailAndPass(email,pass);
        user1 = appDatabase.userDao().getFromEmail(email);

        if (user == null){
            Log.i("TEST", "Login failed....." + email + " " + pass);
            if (user1 == null){
                emailEdit.setError("Email does not exist.");
                wrongEmailToast.show();
            }else {
                passEdit.setError("Password is not correct.");
                wrongPassToast.show();
            }
        }else {
            user.setLoggedIn(true);
            startActivity(intent);
            Log.i("TEST", "Winner winner you locked in bitch..");
        }


    }


}
