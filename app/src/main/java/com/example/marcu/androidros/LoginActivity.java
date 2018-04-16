package com.example.marcu.androidros;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    Intent intent = new Intent();
    EditText emailEdit;
    EditText passEdit;
    String email;
    String pass;
    Context context;
    AppDatabase appDatabase;
    User user;

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
        email = emailEdit.getText().toString();
        pass = passEdit.getText().toString();

        user = appDatabase.userDao().getFromEmailAndPass(email,pass);

        if (user == null){
            Log.i("TEST", "Login failed....." + email + pass);

        }else {
            user.setLoggedIn(true);
            startActivity(intent);
            Log.i("TEST", "Winner winner you locked in bitch..");
        }


    }


}
