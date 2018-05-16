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
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity{

    FirebaseDatabase database;
    String TAG = "ListActivity";
    ArrayList<Event> events = new ArrayList<>();
    List<String> eventIDs = new ArrayList<>();
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
                    eventIDs.add(String.valueOf(postSnapshot.child("eventID").getValue()));
                    //Log.i(CLASS_TAG, "EventID " + event.getEventID());
                }

                for (int i = 0; i < eventIDs.size(); i++){
                    Event event = dataSnapshot.child(eventIDs.get(i)).getValue(Event.class);
                    events.add(event);
                }

                for (int i = 0; i < eventIDs.size(); i++){
                    Log.i(TAG, eventIDs.get(i));
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
        adapter.addFragment(new NearbyFragment());
        adapter.addFragment(new NewFragment());
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
