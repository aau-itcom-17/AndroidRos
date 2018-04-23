package com.example.marcu.androidros;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcu.androidros.utils.Prefs;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    public static FirebaseAuth mAuth;
    public static final String TAG = "AnonymousAuth";

    private Prefs prefs;



    private static final String EMAIL = "email";

    Intent intent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // prefs = Application.getApp().getPrefs();
       // if (prefs.isFirstTimeLaunch()) {
            launchIntroScreen();
            finish();
        //}

        mAuth = FirebaseAuth.getInstance();


        onStart();
        // checking if already logged in
//        sp = getSharedPreferences("login",MODE_PRIVATE);
//        if(sp.getBoolean("logged",false)){
//            intent.setClass(this, MapTestActivity.class);
//            startActivity(intent);
//        }




        setContentView(R.layout.activity_main);
        CallbackManager callbackManager = CallbackManager.Factory.create();


        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
       // loginButton.setReadPermissions(Arrays.asList("user_photos"));
        //loginButton.setReadPermissions(Arrays.asList("user_friends"));

        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                System.out.println("Login");
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                //handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                System.out.println("Login");
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                System.out.println("Login");
                Log.d(TAG, "facebook:onError");
            }
        });

       /* @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }*/




    }

    private void launchIntroScreen() {
        //prefs.setFirstTimeLaunch(false);
        startActivity(new Intent(MainActivity.this, IntroActivity.class));
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public static void updateUI(FirebaseUser user) {
        //hideProgressDialog();

        //TextView idView = findViewById(R.id.anonymous_status_id);
        //TextView emailView = findViewById(R.id.anonymous_status_email);
        boolean isSignedIn = (user != null);

        // Status text
       /* if (isSignedIn) {
            idView.setText(getString(R.string.id_fmt, user.getUid()));
            emailView.setText(getString(R.string.email_fmt, user.getEmail()));
        } else {
            idView.setText(R.string.signed_out);
            emailView.setText(null);
        }

        // Button visibility
        findViewById(R.id.button_anonymous_sign_in).setEnabled(!isSignedIn);
        findViewById(R.id.button_anonymous_sign_out).setEnabled(isSignedIn);
        findViewById(R.id.button_link_account).setEnabled(isSignedIn);*/
    }

    public void nextActivityButtonClicked (View view){
        intent.setClass(this, CreateAccountActivity.class);
        startActivity(intent);
        Log.i("TEST", "Going to CreateAccount");

    }
    public void loginActivityButtonClicked (View view){
        intent.setClass(this,LoginActivity.class);
        startActivity(intent);
        Log.i("TEST", "Going to Login Page");
    }
}
