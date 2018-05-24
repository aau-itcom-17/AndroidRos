package com.example.marcu.androidros.Favourites;

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
import android.widget.Button;
import android.widget.ImageView;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.List.EventInfoActivity;
import com.example.marcu.androidros.List.TopFragment;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.EventPopUp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyEventsFragment extends Fragment implements EventAdapter.OnEventClickListener{

    private String TAG = "MyEventsFragment";
    private RecyclerView recyclerView;
    private EventAdapter myAdapter;
    private ArrayList<Event> allEvents = new ArrayList<>();
    private ArrayList<Event> myEvents = new ArrayList<>();
    private DatabaseReference mDatabaseRef;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.top_fragment_recycler);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        if(getArguments() != null)
        {
            allEvents = getArguments().getParcelableArrayList("key");
            for (int i = 0; i < allEvents.size(); i++) {
                Log.i(TAG, allEvents.get(i).getName());
            }
        }
        else{
            Log.i(TAG, "getArguments = null");
        }


        for (int i = 0; i < allEvents.size(); i++)
        {
            if (allEvents.get(i) != null && allEvents.get(i).getEventOwner() != null) {
                if (allEvents.get(i).getEventOwner().equals(FirebaseAuth.getInstance().getUid())) {
                    Event event = allEvents.get(i);
                    myEvents.add(event);
                }
            }
        }
            myAdapter = new EventAdapter(getActivity(), myEvents);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(myAdapter);
            myAdapter.setOnEventClickListener(MyEventsFragment.this);

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
    public void onEventClick(int position) {
        Intent eventDetailsIntent = new Intent(getActivity(), EventInfoActivity.class);
        Event clickedEvent = myEvents.get(position);

        eventDetailsIntent.putExtra("clickedEvent", clickedEvent );
        startActivity(eventDetailsIntent);
    }

    @Override
    public void onFavouriteClick(int position){

        Event event = myEvents.get(position);
        String eventId = event.getEventID();
        System.out.println("Favourite!");

        mDatabaseRef.child("events").child(eventId).child("favourites").child(FirebaseAuth.getInstance().getUid()).setValue(FirebaseAuth.getInstance().getUid());
        mDatabaseRef.child("users").child(FirebaseAuth.getInstance().getUid()).child("favourites").child(eventId).setValue(eventId);
    }

    @Override
    public void onUnFavouriteClick(int position){

        Event event = myEvents.get(position);
        String eventId = event.getEventID();
        System.out.println("UnFavourite!");

        mDatabaseRef.child("events").child(eventId).child("favourites").child(FirebaseAuth.getInstance().getUid()).removeValue();
        mDatabaseRef.child("users").child(FirebaseAuth.getInstance().getUid()).child("favourites").child(eventId).removeValue();
    }
}
