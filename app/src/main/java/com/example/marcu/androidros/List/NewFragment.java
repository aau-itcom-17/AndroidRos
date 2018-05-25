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
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NewFragment extends Fragment implements MainAdapter.OnEventClickListener{

    String TAG = "NewFragment";
    RecyclerView recyclerView;
    MainAdapter adapter;
    ArrayList<Event> events;
    ImageButton favouriteButton;
    ImageButton unFavouriteButton;
    private DatabaseReference mDatabaseRef;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.top_fragment_recycler);
        favouriteButton = (ImageButton) view.findViewById(R.id.favourite_button);
        unFavouriteButton = (ImageButton) view.findViewById(R.id.favourite_button_clicked);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        if(getArguments() != null) {
            events = getArguments().getParcelableArrayList("key");
            Collections.sort(events, new Comparator<Event>() {
                public int compare(Event m1, Event m2) {
                    return (m1.getDate() + " " + m1.getTime()).compareTo(m2.getDate()+ " " + m2.getTime());
                }
            });


            for (int i = 0; i < events.size(); i++) {
                Log.i(TAG, events.get(i).getDate() + " "+  events.get(i).getTime());
            }

        }else{
            Log.i(TAG, "getArguments = null");
        }


        adapter = new MainAdapter( events);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnEventClickListener(NewFragment.this);

        return view;

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
        String eventId = event.getEventID();

        mDatabaseRef.child("events").child(eventId).child("likes").setValue(event.getLikes() + 1);
        mDatabaseRef.child("events").child(eventId).child("favourites").child(FirebaseAuth.getInstance().getUid()).setValue(FirebaseAuth.getInstance().getUid());
        mDatabaseRef.child("users").child(FirebaseAuth.getInstance().getUid()).child("favourites").child(eventId).setValue(eventId);

    }

    @Override
    public void onUnFavouriteClick(int position) {
        Event event = events.get(position);
        String eventId = event.getEventID();
        mDatabaseRef.child("events").child(eventId).child("likes").setValue(event.getLikes() - 1);
        mDatabaseRef.child("events").child(eventId).child("favourites").child(FirebaseAuth.getInstance().getUid()).removeValue();
        mDatabaseRef.child("users").child(FirebaseAuth.getInstance().getUid()).child("favourites").child(eventId).removeValue();
    }
}