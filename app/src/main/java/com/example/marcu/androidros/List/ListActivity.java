package com.example.marcu.androidros.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.BottomNavigationViewHelper;
import com.example.marcu.androidros.Utils.SectionsPagerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ListActivity extends AppCompatActivity{

    FirebaseDatabase database;
    String TAG = "ListActivity";
    ArrayList<Event> events = new ArrayList<>();
    TopFragment topFragment;
    NearbyFragment nearbyFragment;
    NewFragment newFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        database = FirebaseDatabase.getInstance();
        database.getReference("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String id = String.valueOf(postSnapshot.child("eventID").getValue());
                    Event event = dataSnapshot.child(id).getValue(Event.class);

                    if (event != null) {
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        Date today = Calendar.getInstance().getTime();
                        Date eventDate = null;
                        DateFormat eventDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                        try {
                            eventDate = eventDateFormat.parse(event.getDate() + " " + event.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (today.after(eventDate)) {
                            Log.i(TAG, "Event was too old: " + event.getEventID());
                        } else {
                            // adding to list
                            events.add(event);
                            Log.i(TAG, "Event was added: " + event.getEventID());
                        }
                    }


                }

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("key", events);
                topFragment = new TopFragment();
                nearbyFragment = new NearbyFragment();
                newFragment = new NewFragment();
                topFragment.setArguments(bundle);
                nearbyFragment.setArguments(bundle);
                newFragment.setArguments(bundle);


                setUpBottomNavigationView();
                setUpViewPager();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    //This is setting up the 3 tabs at the top
    private void setUpViewPager (){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(topFragment);
        adapter.addFragment(nearbyFragment);
        adapter.addFragment(newFragment);
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(R.string.title_fragment_top);
        tabLayout.getTabAt(1).setText(R.string.title_fragment_nearby);
        tabLayout.getTabAt(2).setText(R.string.title_fragment_new);


    }

    private void setUpBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(ListActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }
}
