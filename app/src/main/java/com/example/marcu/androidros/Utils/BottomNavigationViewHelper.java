package com.example.marcu.androidros.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.marcu.androidros.Create.CreateActivity;
import com.example.marcu.androidros.Favourites.FavouriteActivity;
import com.example.marcu.androidros.List.ListActivity;
import com.example.marcu.androidros.Map.MapActivity;
import com.example.marcu.androidros.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_map:
                        Intent intent1 = new Intent(context, MapActivity.class);
                        context.startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.ic_list:
                        Intent intent2 = new Intent(context, ListActivity.class);
                        context.startActivity(intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.ic_favorite:
                        Intent intent3 = new Intent(context, FavouriteActivity.class);
                        context.startActivity(intent3.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
//                    case R.id.ic_add:
//                        Intent intent4 = new Intent(context, CreateActivity.class);
//                        context.startActivity(intent4.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
//                        break;

                }


                return false;
            }
        });
    }

}
