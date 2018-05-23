package com.example.marcu.androidros.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.marcu.androidros.Create.LocationTrack;
import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.Map.MapActivity;
import com.example.marcu.androidros.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NearbyFragment extends Fragment implements NearbyAdapter.OnEventClickListener {
    String TAG = "NearbyFragment";
    RecyclerView recyclerView;
    NearbyAdapter adapter;
    ArrayList<Event> events;
    ImageButton favouriteButton;
    ImageButton unFavouriteButton;

    LocationTrack locationTracker;
    double longitude;
    double latitude;
    double distance;

    private DatabaseReference mDatabaseRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.nearby_fragment_recycler);
        favouriteButton = (ImageButton) view.findViewById(R.id.nearby_fragment_favourite_button);
        unFavouriteButton = (ImageButton) view.findViewById(R.id.nearby_fragment_favourite_button_clicked);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        locationTracker = new LocationTrack(getActivity());
        if (locationTracker.canGetLocation()) {

            longitude = locationTracker.getLongitude();
            latitude = locationTracker.getLatitude();

        } else {
            locationTracker.showSettingsAlert();
        }
        locationTracker.stopListener();

        if(getArguments() != null) {
            events = getArguments().getParcelableArrayList("key");

            Collections.sort(events, new Comparator<Event>() {
                public int compare(Event m1, Event m2) {
                    return Double.compare(distance(m1.getLatitude(), m1.getLongitude(), latitude, longitude)
                            ,distance(m2.getLatitude(), m2.getLongitude(), latitude, longitude));
                }
            });
            Collections.reverse(events);


            for (int i = 0; i < events.size(); i++) {
                distance = distance(events.get(i).getLongitude(), events.get(i).getLatitude(), latitude, longitude) * 100;
                int n = (int) distance;
                events.get(i).setDistance(n);
                Log.i(TAG, "Cyka km " + distance);
            }
        }else{
            Log.i(TAG, "getArguments = null");
        }

        adapter = new NearbyAdapter(getActivity(), events);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnEventClickListener(NearbyFragment.this);

        return view;
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        //distance in km:
        dist = dist * 1.609344;

        return (dist);
    }
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    @Override
    public void onEventClick(int position) {
        Intent eventDetailsIntent = new Intent(getActivity(), EventInfoActivity.class);
        Event clickedEvent = events.get(position);
        eventDetailsIntent.putExtra("clickedEvent", clickedEvent );
        startActivity(eventDetailsIntent);
    }

    @Override
    public void onFavouriteClick(int position) {
        Event event = events.get(position);
        String id = event.getEventID();
        System.out.println("Favourite!");
        mDatabaseRef.child("events").child(id).child("favourites").child(FirebaseAuth.getInstance().getUid()).setValue(FirebaseAuth.getInstance().getUid());

    }

    @Override
    public void onUnFavouriteClick(int position) {
        Event event = events.get(position);
        String id = event.getEventID();
        System.out.println("UnFavourite!!!");
        mDatabaseRef.child("events").child(id).child("favourites").child(FirebaseAuth.getInstance().getUid()).removeValue();

    }
}