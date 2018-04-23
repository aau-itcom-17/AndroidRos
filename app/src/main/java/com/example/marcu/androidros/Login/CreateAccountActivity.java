package com.example.marcu.androidros.Login;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcu.androidros.Database.AppDatabase;
import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.UUID;

public class CreateAccountActivity extends AppCompatActivity {


    Intent intent = new Intent();
    Context context;
    CharSequence missingInfoError = "Please fill out all the fields above...";
    CharSequence passwordNotMatching = "Passwords are not matching...";
    CharSequence emailNotValid = "Please provide a valid email...";
    CharSequence emailAlreadyExist = "The email provided already exists...";
    int toastDuration = Toast.LENGTH_LONG;
    Toast missingInfoToast;
    Toast wrongPasswordToast;
    Toast emailNotValidToast;
    Toast emailAlreadyExistToast;
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
        setHideKeyboardOnTouch(this, findViewById(R.id.activity_create_account));
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
        context = getApplicationContext();
        missingInfoToast = Toast.makeText(context, missingInfoError, toastDuration);
        wrongPasswordToast = Toast.makeText(context, passwordNotMatching, toastDuration);
        emailAlreadyExistToast = Toast.makeText(context, emailAlreadyExist, toastDuration);
        emailNotValidToast = Toast.makeText(context, emailNotValid, toastDuration);
        AppDatabase appDatabase = AppDatabase.getDatabase(context);

        if(firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("")){
            missingInfoToast.show();
        }else if (!password.equals(confirmPassword)) {
            editPassword.setText("");
            editConfirmPassword.setText("");
            editPassword.setError("Password doesn't match."); //(text, Drawable icon)
            wrongPasswordToast.show();
        }else if (!isValidEmail(email)) {
            editEmail.setError("This is not a valid email."); // Does not check if the email exist. Only the format example@123.aaa
            emailNotValidToast.show();
        }else if(appDatabase.userDao().getFromEmail(email) != null){
            editEmail.setError("This email is already registered.");
            emailAlreadyExistToast.show();
        }else {
            User user = new User(firstName, lastName, email, password, false);
            appDatabase.userDao().insert(user);
            List<User> users = appDatabase.userDao().getAllUsers();
            for (int i = 0; i < users.size(); i++) {
                Log.i("HEJ", users.get(i).getFirstName() + " " + users.get(i).getUserID());
            }

            startActivity(intent);
        }




    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public static void setHideKeyboardOnTouch(final Context context, View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        try {
            //Set up touch listener for non-text box views to hide keyboard.
            if (!(view instanceof EditText || view instanceof ScrollView)) {

                view.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        return false;
                    }

                });
            }
            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {

                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                    View innerView = ((ViewGroup) view).getChildAt(i);

                    setHideKeyboardOnTouch(context, innerView);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}








