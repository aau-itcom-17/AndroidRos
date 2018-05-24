package com.example.marcu.androidros.List;

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
    private DatabaseReference mDatabaseRef;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.top_fragment_recycler);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();



        if (getArguments() != null) {
            events = getArguments().getParcelableArrayList("key");
            for (int i = 0; i < events.size(); i++) {
                Log.i(TAG, events.get(i).getName());
            }

        }
        else
            {
            Log.i(TAG, "getArguments = null");
            }

        database = FirebaseDatabase.getInstance();
        database.getReference("events").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                Collections.sort(events, new Comparator<Event>() {
                    @Override
                    public int compare(Event o1, Event o2) {

                        return Double.compare((double)dataSnapshot.child(o2.getEventID()).child("favourites").getChildrenCount(),(double)(dataSnapshot.child(o1.getEventID()).child("favourites").getChildrenCount()));
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        adapter = new MainAdapter(getActivity(), events);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnEventClickListener(TopFragment.this);

        return view;

        }



    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
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
        Event clickedEvent = events.get(position);

        eventDetailsIntent.putExtra("clickedEvent", clickedEvent );
        startActivity(eventDetailsIntent);
    }

    @Override
    public void onFavouriteClick(int position){

        Event event = events.get(position);
        String eventId = event.getEventID();
        System.out.println("Favourite!");

        mDatabaseRef.child("events").child(eventId).child("favourites").child(FirebaseAuth.getInstance().getUid()).setValue(FirebaseAuth.getInstance().getUid());
        mDatabaseRef.child("users").child(FirebaseAuth.getInstance().getUid()).child("favourites").child(eventId).setValue(eventId);

    }

    @Override
    public void onUnFavouriteClick(int position){

        Event event = events.get(position);
        String eventId = event.getEventID();
        System.out.println("UnFavourite!");

        mDatabaseRef.child("events").child(eventId).child("favourites").child(FirebaseAuth.getInstance().getUid()).removeValue();
        mDatabaseRef.child("users").child(FirebaseAuth.getInstance().getUid()).child("favourites").child(eventId).removeValue();
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