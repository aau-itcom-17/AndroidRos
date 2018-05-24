package com.example.marcu.androidros.Map;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcu.androidros.Create.CreateActivity;
import com.example.marcu.androidros.Create.LocationTrack;
import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.List.EventInfoActivity;
import com.example.marcu.androidros.Login.MainActivity;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.BottomNavigationViewHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnInfoWindowClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "MapActivity";
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mToggle;
    public GoogleMap mMap;
    private boolean mPermissionDenied = false;
    private static final LatLng ORANGESCENE = new LatLng(55.621441, 12.077196);
    private static final LatLng ROSKILDE = new LatLng(55.616885, 12.077064);
    private static final LatLng HOME = new LatLng(55.650661, 12.525810);
    private LatLng eventLoc;
    private Marker mAAU;
    private static final LatLng ZOOM = new LatLng(55.641010, 12.081299);
    private Marker mOrangeScene;
    private Marker mRoskilde;
    private Marker mHome;
    private Intent intent;

    private LocationTrack locationTracker;
    private LatLng userLoc;

    double longitude, latitude;
    private TextView locationLatitude;
    private TextView locationLongitude;


    private TextView drawerName;
    private TextView drawerMessage;
    private NavigationView navigationView;
    View headerView;
    private ImageView drawerImage;
    private String profilePictureRef;
    private String fullName;
    private String email;
    private File profilePicFile;
    private ChildEventListener mChildEventListener;
    private ImageButton imageButton;


    private final static String SAVE_MAP_STATE = "saveMapState";

    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private ArrayList<Event> events = new ArrayList<>();
    private List<String> eventIDs = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Log.i(TAG, "onCreate");

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //navigationView = (NavigationView) findViewById(R.id.nav_view);
        //headerView = navigationView.getHeaderView(0);
        //drawerImage = (ImageView) headerView.findViewById(R.id.drawerImage);
        //drawerName = (TextView) headerView.findViewById(R.id.drawerName);
        //drawerMessage = (TextView) headerView.findViewById(R.id.drawerMessage);
        imageButton = (ImageButton) findViewById(R.id.event_image_button);

        //navigationView.setNavigationItemSelectedListener(this);

        //mToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        //drawer.addDrawerListener(mToggle);
        //mToggle.syncState();
        ChildEventListener mChildEventListener;

/*

        //Access data in database
        database = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if (firebaseUser != null) {
                    String uid = firebaseUser.getUid();
                    Log.i("Firebase", "uid: " + uid);
                    User user = dataSnapshot.child(uid).getValue(User.class);
                    if (user != null) {
                        fullName = user.getFirstName() + " " + user.getLastName();
                        email = user.getEmail();
                        profilePictureRef = user.getProfilePicture();
                        Log.i("Firebase", "Profile picture reference: " + profilePictureRef);
                        Log.i("Firebase", "Full name: " + fullName);
                    }
                    drawerName.setText(fullName);
                    randomMessage();
                    if (profilePictureRef != null) {
                        profilePicFile = new File(profilePictureRef);
                        if (profilePicFile.exists()) {
                            drawerImage.setImageBitmap(BitmapFactory.decodeFile(profilePictureRef));
                        } else {
                            Toast.makeText(MapActivity.this, "Couldn't load profile picture... " +
                                    "please change your profile picture in account settings.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
        // access done.

*/

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setUpBottomNavigationView();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.roskilde);
        map.setMapStyle(style);

        mMap = map;
        // Here you can set the different types of map types. OOPS! If you do this we loose our custom map type.
        // map.setMapType(map.MAP_TYPE_SATELLITE);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        //mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        enableMyLocation();

        locationTracker = new LocationTrack(MapActivity.this);
        if (locationTracker.canGetLocation()) {

            longitude = locationTracker.getLongitude();
            latitude = locationTracker.getLatitude();
            userLoc = new LatLng(latitude, longitude);

        } else {
            locationTracker.showSettingsAlert();
        }
        locationTracker.stopListener();

        try {
            Intent intent = getIntent();
            eventLoc = intent.getParcelableExtra("event_location");
            Log.i(TAG, "LatLong Data is: " + eventLoc.toString());

        } catch (NullPointerException e) {
            Log.i(TAG, "No LatLong Data");

        }

        if (eventLoc != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLoc, 15));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 14));
        }


        //Creates a polygon around roskilde festival.
        PolygonOptions roskilde = new PolygonOptions()
                .add(new LatLng(55.624404, 12.065872),
                        new LatLng(55.616147, 12.065023),
                        new LatLng(55.610281, 12.061161),
                        new LatLng(55.608612, 12.084448),
                        new LatLng(55.610256, 12.095493),
                        new LatLng(55.624845, 12.091416),
                        new LatLng(55.623390, 12.074894))
                .strokeColor(Color.argb(255, 255, 152, 0));

        Polygon polygon = map.addPolygon(roskilde);


        PolygonOptions festivalArea = new PolygonOptions()
                .add(new LatLng(55.622268, 12.070307),
                        new LatLng(55.617445, 12.072882),
                        new LatLng(55.617460, 12.075009),
                        new LatLng(55.617460, 12.075009),
                        new LatLng(55.618425, 12.086578),
                        new LatLng(55.622810, 12.085591))
                .strokeColor(Color.argb(255, 255, 152, 0));

        Polygon polygon1 = map.addPolygon(festivalArea);

        // Implement info_window_layout.xml - with imageview.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {

                return null;

            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = MapActivity.this.getLayoutInflater().inflate(R.layout.marker_event_info, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.marker_info_image_view);
                TextView title = (TextView) view.findViewById(R.id.marker_info_title_text_view);
                TextView snippet = (TextView) view.findViewById(R.id.marker_event_time);
                Event event = (Event) marker.getTag();


                if (event != null) {
                    String path = event.getPhotoPath();
                    Log.i(TAG, path);
                    title.setText(marker.getTitle());
                    snippet.setText(marker.getSnippet());

                    Picasso.get().load(path).into(imageView, new MarkerCallback(marker));

                }


                return view;
            }
        });

        // Adding markers on map. See method down below.
        addMarkersOnMap();
    }
    public class MarkerCallback implements Callback {
        Marker marker;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError(Exception e) {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }


    //Click listener for my location button.
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "This is your current location", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.

            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        SharedPreferences prefs = getSharedPreferences(SAVE_MAP_STATE, MODE_PRIVATE);
        //String restoredEmail = prefs.getString("map", null);
        //if (restoredEmail != null && emailFromCreateAccount == null) {
        //     emailEdit.setText(restoredEmail);
        //}

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        SharedPreferences.Editor editor = getSharedPreferences(SAVE_MAP_STATE, MODE_PRIVATE).edit();
        //editor.putString("email", emailEdit.getText().toString());
        editor.apply();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

/*

    // method controlling the menu buttons under the user info in the drawer.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_userpage:
//                getSupportFragmentManager().beginTransaction().replace(R.id.layout_body, new UserPage()).commit();
                Intent k = new Intent(this, UserPage.class);
                startActivity(k);
                break;
            case R.id.nav_logout:
                // logging out
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MapActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(MapActivity.this);
                }
                builder.setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //signing out
                                FirebaseAuth.getInstance().signOut();
                                intent = new Intent(MapActivity.this, MainActivity.class);
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
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //When the back button is pressed on the drawer only the drawer will close and not the activity you have chosen
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
    */

    private void setUpBottomNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(MapActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(MapActivity.this, EventInfoActivity.class);
        Event event = (Event) marker.getTag();
        intent.putExtra("clickedEvent", event);
        startActivity(intent);

    }

    private void randomMessage() {
        String[] messages;
        messages = getResources().getStringArray(R.array.messages);
        SecureRandom secureRandom = new SecureRandom();
        int number = secureRandom.nextInt(messages.length);
        drawerMessage.setText(messages[number]);
    }

    public void createEventOnClicked(View view) {
        intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }

    public void addMarkersOnMap() {
        database = FirebaseDatabase.getInstance();
        database.getReference("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    eventIDs.add(String.valueOf(postSnapshot.child("eventID").getValue()));
                }
                for (int i = 0; i < eventIDs.size(); i++) {
                    Event event = dataSnapshot.child(eventIDs.get(i)).getValue(Event.class);
                    events.add(event);

                    if (event != null) {
                        LatLng eventPos = new LatLng(event.getLatitude(), event.getLongitude());


                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        Date today = Calendar.getInstance().getTime();
                        String reportDate = df.format(today);

                        String[] separatedDate = reportDate.split(" ", 2);
                        String date = separatedDate[0];
                        String time = separatedDate[1];
                        String[] separated = date.split("/");
                        String[] separatedTime = time.split(":");
                        String day = separated[0];
                        String month = separated[1];
                        String year = separated[2];
                        String hour = separatedTime[0];
                        String minutes = separatedTime[1];

                        String[] separatedEvent = event.getDate().split("/");
                        String[] separatedEventTime = event.getTime().split(":");
                        String eventDay = separatedEvent[0];
                        String eventMonth = separatedEvent[1];
                        String eventYear = separatedEvent[2];
                        String eventHour = separatedEventTime[0];
                        String eventMinutes = separatedEventTime[1];

                        String snippetTime;
                        boolean lazyBoolean = false;
                        if (Integer.parseInt(day) > Integer.parseInt(eventDay)){
                            if (Integer.parseInt(hour) + (Double.parseDouble(minutes)/100) < Integer.parseInt(eventHour) + (Double.parseDouble(eventMinutes)/100) - 24 + 3) {
                                snippetTime = "Began earlier, " + event.getTime();
                            } else {
                                snippetTime = "Yesterday, " + event.getTime();
                                lazyBoolean = true;
                            }
                        } else if (Integer.parseInt(day) == Integer.parseInt(eventDay)) {
                            if (Integer.parseInt(hour) + (Double.parseDouble(minutes) / 100) > Integer.parseInt(eventHour) + (Double.parseDouble(eventMinutes) / 100) + 3) {
                                snippetTime = "Began earlier, " + event.getTime();
                            } else if (Integer.parseInt(hour) + (Double.parseDouble(minutes) / 100) < Integer.parseInt(eventHour) + (Double.parseDouble(eventMinutes) / 100) + 3
                                    && Integer.parseInt(hour) + (Double.parseDouble(minutes) / 100) > Integer.parseInt(eventHour) + (Double.parseDouble(eventMinutes) / 100)) {
                                snippetTime = "Now, " + event.getTime();
                            } else if (18 < Integer.parseInt(eventHour) && Integer.parseInt(eventHour) < 24) {
                                snippetTime = "Tonight, " + event.getTime();
                            } else {
                                snippetTime = "Today, " + event.getTime();
                            }
                        } else if (Integer.parseInt(day) == Integer.parseInt(eventDay) - 1) {
                            snippetTime = "Tomorrow, " + event.getTime();
                        } else {
                            snippetTime = event.getDate() + " " + event.getTime();
                        }

                        //Checking if it's more than 3 hours old
                        if (Integer.parseInt(day) > Integer.parseInt(eventDay)
                                && Integer.parseInt(hour) + (Double.parseDouble(minutes)/100) < Integer.parseInt(eventHour) + (Double.parseDouble(eventMinutes)/100) - 24 + 3
                                || lazyBoolean == true || Integer.parseInt(day) == Integer.parseInt(eventDay)
                                && Integer.parseInt(hour) + (Double.parseDouble(minutes) / 100) > Integer.parseInt(eventHour) + (Double.parseDouble(eventMinutes) / 100) + 3){
                            Log.i(TAG, "Event: " + event.getName() + " is more than 3 hours old, and will not be placed on the map");
                        } else {
                            Marker eventMarker = mMap.addMarker(new MarkerOptions()
                                    .position(eventPos)
                                    .title(event.getName())
                                    .snippet(snippetTime)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                            eventMarker.setTag(event);
                            // Tag makes it possible to send the event to another activity. onInfoWindowClick()

                        }

                    } else {
                        // null pointer throw error
                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}