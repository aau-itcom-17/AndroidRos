package com.example.marcu.androidros.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.marcu.androidros.Likes.LikesActivity;
import com.example.marcu.androidros.Main.MainActivity;
import com.example.marcu.androidros.Profile.ProfileActivity;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Search.SearchActivity;
import com.example.marcu.androidros.Share.ShareActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
// Making this class, to not repeat writing for each tab in button navigation.
public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";

    // This method gives a better view for the navigationbar.
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
    }

    // How will we navigate between the activities
    public static void enableNavigation(final Context context, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_house:
                        Intent intent1 = new Intent(context, MainActivity.class);//The method is final because of the @Override //ACTIVITY_NUM = 0
                        context.startActivity(intent1); //We have the context because we are in an object class
                        break;
                    case R.id.ic_search:
                        Intent intent2 = new Intent(context, SearchActivity.class);
                        context.startActivity(intent2);
                        break;
                    case R.id.ic_circle:
                        Intent intent3 = new Intent(context, ShareActivity.class);
                        context.startActivity(intent3);
                        break;
                    case R.id.ic_alert:
                        Intent intent4 = new Intent(context, LikesActivity.class);
                        context.startActivity(intent4);
                        break;
                    case R.id.ic_android:
                        Intent intent5 = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent5);
                        break;

                }

                return false;
            }
        });
    }



}
