package com.example.marcu.androidros.Login;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marcu.androidros.Login.MainActivity;
import com.example.marcu.androidros.Map.MapActivity;
import com.example.marcu.androidros.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginWithFirebaseActivity extends AppCompatActivity {

    Intent intent = new Intent();
    EditText emailEdit;
    EditText passEdit;
    String email;
    String password;
    Context context;
    int toastDuration = Toast.LENGTH_SHORT;
    CharSequence wrongEmailAndPass = "The Email and/or Password does not exist.";
    private String emailFromCreateAccount;
    private static final String EXTRA_ID = "emailID";
    private static final String SAVE_EMAIL = "saveEMAIL";

    String TAG = "Firebase";
    String CLASS_TAG = "LoginActivity";

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference myDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_firebase);

        CreateAccountActivity.setHideKeyboardOnTouch(this, findViewById(R.id.activity_login_with_firebase));
        emailEdit = findViewById(R.id.emailEditLogin);
        passEdit = findViewById(R.id.passEditLogin);
        context = getApplicationContext();

        //getting email from create account
        emailFromCreateAccount = getIntent().getStringExtra(EXTRA_ID);
        Log.i("FROM_CREATE",  "email: "+  emailFromCreateAccount);
        // saves email if you go back to main menu


        emailEdit.setText(emailFromCreateAccount);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myDatabaseRef = database.getReference();

    }

    public void loginButton (View view){
        intent.setClass(this, MapActivity.class);
        email = emailEdit.getText().toString();
        password = passEdit.getText().toString();

        signInWithFirebase(email, password);

        // ...
    }

    private void signInWithFirebase(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            startActivity(intent);
                            MainActivity.getInstance().finish();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, wrongEmailAndPass, toastDuration).show();
                            emailEdit.setError(wrongEmailAndPass);
                        }

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(CLASS_TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(CLASS_TAG, "onResume");
        SharedPreferences prefs = getSharedPreferences(SAVE_EMAIL, MODE_PRIVATE);
        String restoredEmail = prefs.getString("email", null);
        Log.i(CLASS_TAG, "Email from pref: " + restoredEmail);
        if (restoredEmail != null && emailFromCreateAccount == null) {
            emailEdit.setText(restoredEmail);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences(SAVE_EMAIL, MODE_PRIVATE).edit();
        editor.putString("email", emailEdit.getText().toString());
        editor.apply();
        Log.i(CLASS_TAG, "The editor email in onPause: " + emailEdit.getText().toString());
        Log.i(CLASS_TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(CLASS_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(CLASS_TAG, "onDestroy");
    }

}

