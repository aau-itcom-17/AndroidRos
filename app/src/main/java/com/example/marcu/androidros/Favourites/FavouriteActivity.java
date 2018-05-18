package com.example.marcu.androidros.Favourites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.List.NearbyFragment;
import com.example.marcu.androidros.List.NewFragment;
import com.example.marcu.androidros.List.TopFragment;
import com.example.marcu.androidros.Utils.SectionsPagerAdapter;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.BottomNavigationViewHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    /*@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        setUpBottomNavigationView();
        setUpViewPager();
    }*/


    FirebaseDatabase database;
    String TAG = "FavouriteActivity";
    ArrayList<Event> myEvents = new ArrayList<>();
    List<String> myEventIDs = new ArrayList<>();
    MyEventsFragment myEventsFragment;
    MyFavouritesFragment myFavouritesFragment;
    MyInvitesFragment myInvitesFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        database = FirebaseDatabase.getInstance();
        database.getReference("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    myEventIDs.add(String.valueOf(postSnapshot.child("eventID").getValue()));
                    //Log.i(CLASS_TAG, "EventID " + event.getEventID());
                }

                for (int i = 0; i < myEventIDs.size(); i++){
                    Event myEvent = dataSnapshot.child(myEventIDs.get(i)).getValue(Event.class);
                    myEvents.add(myEvent);
                }

                for (int i = 0; i < myEventIDs.size(); i++){
                    Log.i(TAG, myEventIDs.get(i));
                }

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("key", myEvents);
                myEventsFragment = new MyEventsFragment();
                myFavouritesFragment = new MyFavouritesFragment();
                myInvitesFragment = new MyInvitesFragment();
                myEventsFragment.setArguments(bundle);
                myFavouritesFragment.setArguments(bundle);
                myInvitesFragment.setArguments(bundle);


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
        SectionsPagerAdapter myAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        myAdapter.addFragment(myEventsFragment);
        myAdapter.addFragment(new MyFavouritesFragment());
        myAdapter.addFragment(new MyInvitesFragment());
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(myAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(R.string.title_my_events);
        tabLayout.getTabAt(1).setText(R.string.title_my_favourites);
        tabLayout.getTabAt(2).setText(R.string.title_my_invites);


    }

    private void setUpBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(FavouriteActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }
}

