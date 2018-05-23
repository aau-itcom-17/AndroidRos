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

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NearbyFragment extends Fragment implements NearbyAdapter.OnEventClickListener {
    String TAG = "TopFragment";
    RecyclerView recyclerView;
    NearbyAdapter adapter;
    ArrayList<Event> events;
    ImageButton favouriteButton;
    ImageButton unFavouriteButton;

    private DatabaseReference mDatabaseRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.nearby_fragment_recycler);
        favouriteButton = (ImageButton) view.findViewById(R.id.nearby_fragment_favourite_button);
        unFavouriteButton = (ImageButton) view.findViewById(R.id.nearby_fragment_favourite_button_clicked);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        if(getArguments() != null) {
            events = getArguments().getParcelableArrayList("key");
            for (int i = 0; i < events.size(); i++) {
                Log.i(TAG, events.get(i).getName());
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