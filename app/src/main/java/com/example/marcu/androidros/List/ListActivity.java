package com.example.marcu.androidros.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.marcu.androidros.Create.LocationTrack;
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
    LocationTrack locationTracker;
    double longitude;
    double latitude;
    double distance;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        database = FirebaseDatabase.getInstance();
        database.getReference("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.HOUR_OF_DAY, - 3);
                Date today = cal.getTime();

                Log.i(TAG, "MOTHERFUCKING TIME - 3 hours" + today);
                Date eventDate = null;
                DateFormat eventDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                locationTracker = new LocationTrack(ListActivity.this);
                if (locationTracker.canGetLocation()) {
                    longitude = locationTracker.getLongitude();
                    latitude = locationTracker.getLatitude();
                } else {
                    locationTracker.showSettingsAlert();
                }
                locationTracker.stopListener();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String id = String.valueOf(postSnapshot.child("eventID").getValue());
                    Event event = dataSnapshot.child(id).getValue(Event.class);
                    if (event != null) {

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
                for (int i = 0; i < events.size(); i++) {
                    distance = distance(events.get(i).getLatitude(), events.get(i).getLongitude(), latitude, longitude);
                    events.get(i).setDistance((int) distance);
                    Log.i(TAG, "Cast to int " + events.get(i).getName() + " " + events.get(i).getDistance());
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

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6378100; //m
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double diffLat = Math.toRadians(lat2-lat1);
        double diffLon = Math.toRadians(lon2-lon1);

        double a = Math.sin(diffLat/2) * Math.sin(diffLat/2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(diffLon/2) * Math.sin(diffLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d =  earthRadius * c;

        return d;
    }


}
