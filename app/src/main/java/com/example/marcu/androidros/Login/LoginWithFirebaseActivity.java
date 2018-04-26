package com.example.marcu.androidros.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginWithFirebaseActivity extends AppCompatActivity {

    Intent intent = new Intent();
    public static SharedPreferences sp;
    EditText emailEdit;
    EditText passEdit;
    String email;
    String password;
    Context context;
    //AppDatabase appDatabase;
    User user1;
    Toast wrongEmailToast;
    Toast wrongPassToast;
    int toastDuration = Toast.LENGTH_LONG;
    CharSequence wrongEmailAndPass = "The Email and/or Password does not exist.";


    String TAG = "Firebase";
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_firebase);
        CreateAccountActivity.setHideKeyboardOnTouch(this, findViewById(R.id.activity_login));

        emailEdit = findViewById(R.id.emailEdit1);
        passEdit = findViewById(R.id.passEdit1);
        context = getApplicationContext();
        //appDatabase = AppDatabase.getDatabase(context);



    }

    public void loginButton (View view){
        intent.setClass(this, SplashActivity.class);
        email = emailEdit.getText().toString();
        password = passEdit.getText().toString();


        signInWithFirebase(email, password);
        startActivity(intent);

    }

    private void signInWithFirebase(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginWithFirebaseActivity.this, wrongEmailAndPass ,
                                    toastDuration).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}

