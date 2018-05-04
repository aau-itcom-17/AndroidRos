package com.example.marcu.androidros.Map;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.Login.CreateAccountActivity;
import com.example.marcu.androidros.Login.MainActivity;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.BottomNavigationViewHelper;
import com.example.marcu.androidros.Utils.EventPopUp;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.File;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnInfoWindowClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String CLASS_TAG = "MapActivity";
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mToggle;
    public GoogleMap mMap;
    private boolean mPermissionDenied = false;
    private static final LatLng AAU = new LatLng(55.649114, 12.542689);
    private static final LatLng ROSKILDE = new LatLng(55.616885, 12.077064);
    private static final LatLng HOME = new LatLng(55.650661, 12.525810);
    private static final LatLng DK = new LatLng(55.641010, 12.081299);
    private LatLng MyLocation = null;
    private Marker mAAU;
    private Marker mRoskilde;
    private Marker mHome;
    private Intent intent;


    private TextView drawerName;
    private TextView drawerEmail;
    private NavigationView navigationView;
    View headerView;
    private ImageView drawerImage;
    private String profilePictureRef;
    private String fullName;
    private String email;
    private File profilePicFile;

    private final static String SAVE_MAP_STATE = "saveMapState";

    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Log.i(CLASS_TAG, "onCreate");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        drawerEmail = (TextView) headerView.findViewById(R.id.drawerEmail);
        drawerImage = (ImageView) headerView.findViewById(R.id.drawerImage);
        drawerName = (TextView) headerView.findViewById(R.id.drawerName);

        navigationView.setNavigationItemSelectedListener(this);

        mToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(mToggle);
        mToggle.syncState();


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
                    drawerEmail.setText(email);
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

        // Move the camera to location everytime you open up the page.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(DK, 12));

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


        //Creating three simple markers.
        mAAU = mMap.addMarker(new MarkerOptions()
                .position(AAU)
                .title("AAU")
                .snippet("The university")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        mAAU.setTag(0);

        // Example on how to put in a customisable icon for a marker.
        mRoskilde = mMap.addMarker(new MarkerOptions()
                .position(ROSKILDE)
                .snippet("For more information click on this window!")
                .title("Roskilde Festival")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mRoskilde.setTag(0);


        mHome = mMap.addMarker(new MarkerOptions()
                .position(HOME)
                .title("Home")
                .snippet("The location of my home")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mHome.setTag(0);

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
        Log.i(CLASS_TAG, "onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(CLASS_TAG, "onResume");
        SharedPreferences prefs = getSharedPreferences(SAVE_MAP_STATE, MODE_PRIVATE);
        //String restoredEmail = prefs.getString("map", null);
        //if (restoredEmail != null && emailFromCreateAccount == null) {
        //     emailEdit.setText(restoredEmail);
        //}

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(CLASS_TAG, "onPause");
        SharedPreferences.Editor editor = getSharedPreferences(SAVE_MAP_STATE, MODE_PRIVATE).edit();
        //editor.putString("email", emailEdit.getText().toString());
        editor.apply();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(CLASS_TAG, "onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(CLASS_TAG, "onDestroy");
    }


    // method controlling the menu buttons under the user info in the drawer.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_userpage:
                getSupportFragmentManager().beginTransaction().replace(R.id.layout_body, new UserPageFragment()).commit();
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
        if (marker.equals(mRoskilde)) {
            Intent intent = new Intent(MapActivity.this, EventPopUp.class);
            startActivity(intent);
        }
    }
}