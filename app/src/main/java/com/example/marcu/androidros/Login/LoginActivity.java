package com.example.marcu.androidros.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.Map.MapActivity;
import com.example.marcu.androidros.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    Intent intent = new Intent();
    public static SharedPreferences sp;
    EditText emailEdit;
    EditText passEdit;
    String email;
    String pass;
    Context context;
    //AppDatabase appDatabase;
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
        //appDatabase = AppDatabase.getDatabase(context);

    }
/*
    public void loginButton (View view){
        intent.setClass(this, SplashActivity.class);
        wrongEmailToast = Toast.makeText(context, wrongEmail, toastDuration);
        wrongPassToast = Toast.makeText(context, wrongPass,toastDuration);
        email = emailEdit.getText().toString();
        pass = passEdit.getText().toString();

        SplashActivity.user = SplashActivity.appDatabase.userDao().getFromEmailAndPass(email,pass);
        user1 = SplashActivity.appDatabase.userDao().getFromEmail(email);

        if (SplashActivity.user == null){
            Log.i("TEST", "Login failed....." + email + " " + pass);
            if (user1 == null){
                emailEdit.setError("Email does not exist.");
                wrongEmailToast.show();
            }else {
                passEdit.setError("Password is not correct.");
                wrongPassToast.show();
            }
        }else {
            // MainActivity.sp.edit().putBoolean("logged",true).apply();
            SplashActivity.user.setLoggedIn(true);
            SplashActivity.appDatabase.userDao().update(SplashActivity.user);
            SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
            SharedPreferences.Editor edt = pref.edit();
            edt.putBoolean("activity_executed", true);
//            edt.putInt("userÍd", SplashActivity.user.getUserID());
            edt.apply();
            startActivity(intent);
            Log.i("TEST", "Winner winner you locked in bitch..");
        }


    }

*/
}
