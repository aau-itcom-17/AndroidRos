package com.example.marcu.androidros.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.EventPopUp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class TopFragment extends Fragment implements MainAdapter.OnEventClickListener {

    private FirebaseDatabase database;
    String TAG = "TopFragment";
    RecyclerView recyclerView;
    MainAdapter adapter;
    ArrayList<Event> events;
    ArrayList<Event> eventsTest=new ArrayList<Event>();
    ArrayList<Event> eventsUpdated;
    private DatabaseReference mDatabaseRef;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            events = getArguments().getParcelableArrayList("key");
        }
        else
        {
            Log.i(TAG, "getArguments = null");
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Collections.sort(events, new Comparator<Event>() {
                    @Override
                    public int compare(Event o1, Event o2) {
                        return (Integer.toString(o2.getLikes()).compareTo(Integer.toString(o1.getLikes())));
                    }
                });


            }
        });
        thread.start();

        Thread[] threads = new Thread[1];
        threads[0] = thread;

        for (int i = 0; i < threads.length; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < events.size(); i++){
            Log.i(TAG,events.get(i).getName() + events.get(i).getLikes());
        }

        adapter = new MainAdapter(eventsTest);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.top_fragment_recycler);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnEventClickListener(TopFragment.this);


        final Activity act = getActivity(); //only necessary if you use fragments
        if (act != null)
            act.runOnUiThread(new Runnable() {
                public void run() {
                    eventsUpdated = adapter.updateItems(events);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    Log.i(TAG, "Adapter is updated " + adapter.getItemCount());
                }
            });
        Log.i(TAG, "onStart");


        Log.i(TAG, "Returning view");

        return view;

        }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");

    }

    @Override
    public void onEventClick(int position) {
        Intent eventDetailsIntent = new Intent(getActivity(), EventInfoActivity.class);
        Event clickedEvent = eventsUpdated.get(position);

        eventDetailsIntent.putExtra("clickedEvent", clickedEvent );
        startActivity(eventDetailsIntent);
    }

    @Override
    public void onFavouriteClick(int position){

        Event event = eventsUpdated.get(position);
        String eventId = event.getEventID();
        System.out.println("Favourite!");

        mDatabaseRef.child("events").child(eventId).child("likes").setValue(event.getLikes() + 1);
        mDatabaseRef.child("users").child(FirebaseAuth.getInstance().getUid()).child("favourites").child(eventId).setValue(eventId);
        event.setLikes(event.getLikes()+1);
    }

    @Override
    public void onUnFavouriteClick(int position){

        Event event = eventsUpdated.get(position);
        String eventId = event.getEventID();
        System.out.println("UnFavourite!");

        mDatabaseRef.child("events").child(eventId).child("likes").setValue(event.getLikes() - 1);
        mDatabaseRef.child("users").child(FirebaseAuth.getInstance().getUid()).child("favourites").child(eventId).removeValue();
        event.setLikes(event.getLikes()-1);
    }





//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            //case R.id.event_button:
//            //   startActivity(new Intent(getActivity(), EventPopUp.class));
//            //   break;
//        }
//    }

}