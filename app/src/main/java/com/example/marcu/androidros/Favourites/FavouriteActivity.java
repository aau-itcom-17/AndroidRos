package com.example.marcu.androidros.Favourites;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.Login.MainActivity;
import com.example.marcu.androidros.Utils.SectionsPagerAdapter;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    List<String> eventIDs = new ArrayList<>();
    MyEventsFragment myEventsFragment;
    MyFavouritesFragment myFavouritesFragment;
    MyInvitesFragment myInvitesFragment;
    private Intent intent;
    private FirebaseUser firebaseUser;
    private String fullName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        Toolbar actionbar = (Toolbar) findViewById(R.id.action_bar_user_page);
        setSupportActionBar(actionbar);

        database.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (firebaseUser != null) {
                    String id = firebaseUser.getUid();
                    User user = dataSnapshot.child(id).getValue(User.class);
                    if (user != null) {
                        fullName = user.getFirstName() + " " + user.getLastName();
                    }
                    getSupportActionBar().setTitle(fullName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        database.getReference("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    if (String.valueOf(postSnapshot.child("eventOwner").getValue()).equals(FirebaseAuth.getInstance().getUid())) {
                        eventIDs.add(String.valueOf(postSnapshot.child("eventID").getValue()));
                    }

                    //Log.i(CLASS_TAG, "EventID " + event.getEventID());
                }

                for (int i = 0; i < eventIDs.size(); i++) {
                    Event myEvent = dataSnapshot.child(eventIDs.get(i)).getValue(Event.class);
                    myEvents.add(myEvent);
                }

                for (int i = 0; i < eventIDs.size(); i++) {
                    Log.i(TAG, eventIDs.get(i));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                //Toast.makeText(this, "User Page", Toast.LENGTH_SHORT).show();
                Intent userPageIntent = new Intent (FavouriteActivity.this, UserPage.class);
                FavouriteActivity.this.startActivity(userPageIntent);
                return true;
            case R.id.item2:
                // logging out
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(FavouriteActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(FavouriteActivity.this);
                }
                builder.setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //signing out
                                FirebaseAuth.getInstance().signOut();
                                intent = new Intent(FavouriteActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    //This is setting up the 3 tabs at the top
    private void setUpViewPager() {
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

    private void setUpBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(FavouriteActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }
}

