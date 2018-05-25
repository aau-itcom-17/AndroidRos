package com.example.marcu.androidros.List;

import android.app.Activity;
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

import com.example.marcu.androidros.Create.LocationTrack;
import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.R;
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
    ArrayList<Event> eventsTetst=new ArrayList<Event>();
    ArrayList<Event> eventsUpdated;
    ImageButton favouriteButton;
    ImageButton unFavouriteButton;

    LocationTrack locationTracker;
    double longitude;
    double latitude;
    double distance;

    private DatabaseReference mDatabaseRef;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationTracker = new LocationTrack(getActivity());
        if (locationTracker.canGetLocation()) {
            longitude = locationTracker.getLongitude();
            latitude = locationTracker.getLatitude();
        } else {
            locationTracker.showSettingsAlert();
        }
        locationTracker.stopListener();

        if (getArguments() != null) {
            events = getArguments().getParcelableArrayList("key");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Collections.sort(events, new Comparator<Event>() {
                        public int compare(Event m1, Event m2) {
                            return Double.compare(distance(m1.getLatitude(), m1.getLongitude(), latitude, longitude)
                                    , (distance(m2.getLatitude(), m2.getLongitude(), latitude, longitude)));
                        }
                    });
                }
            });
            thread.start();

            Thread[] threads = new Thread[1];
            threads[0] = thread;

            for (int i = 0; i < threads.length; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < events.size(); i++) {
                distance = distance(events.get(i).getLatitude(), events.get(i).getLongitude(), latitude, longitude);
                events.get(i).setDistance((int) distance);
                Log.i(TAG, "Cast to int " + events.get(i).getName() + " " + events.get(i).getDistance());
            }
        }else{
                Log.i(TAG, "getArguments = null");
            }

            // 1.
        adapter = new NearbyAdapter(eventsTetst);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.nearby_fragment_recycler);
        favouriteButton = (ImageButton) view.findViewById(R.id.nearby_fragment_favourite_button);
        unFavouriteButton = (ImageButton) view.findViewById(R.id.nearby_fragment_favourite_button_clicked);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnEventClickListener(NearbyFragment.this);



            final Activity act = getActivity(); //only neccessary if you use fragments
            if (act != null)
                act.runOnUiThread(new Runnable() {
                    public void run() {
                        eventsUpdated = adapter.updateItems(events);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        Log.i(TAG, "Adapter is updated " + adapter.getItemCount());
                    }
                });


            Log.i(TAG, "View is returned");
            return view;


    }
    public void onResume() {
        super.onResume();
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






    @Override
    public void onEventClick(int position) {
        Intent eventDetailsIntent = new Intent(getActivity(), EventInfoActivity.class);
        Event clickedEvent = eventsUpdated.get(position);
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