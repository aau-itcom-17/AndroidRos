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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.List.EventInfoActivity;
import com.example.marcu.androidros.Map.MapActivity;
import com.example.marcu.androidros.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyFavouritesFragment extends Fragment implements EventAdapter.OnEventClickListener {


    String TAG = "MyFavouritesFragment";
    RecyclerView recyclerView;
    EventAdapter myAdapter;
    ArrayList<Event> myFavourites;
    ImageView favouriteButton;
    ImageView favouriteButtonClicked;
    private DatabaseReference mDatabaseRef;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.top_fragment_recycler);
        favouriteButton = (ImageView) view.findViewById(R.id.favourite_button);
        favouriteButtonClicked = (ImageView) view.findViewById(R.id.favourite_button_clicked);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        if(getArguments() != null) {
            myFavourites = getArguments().getParcelableArrayList("key");
            System.out.println("Third: " + myFavourites.size());

            for (int i = 0; i < myFavourites.size(); i++) {
                Log.i(TAG, myFavourites.get(i).getName());
            }
        }else{
            Log.i(TAG, "getArguments = null");
        }


        myAdapter = new EventAdapter(getActivity(), myFavourites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnEventClickListener(MyFavouritesFragment.this);

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
        Event clickedEvent = myFavourites.get(position);

        eventDetailsIntent.putExtra("clickedEvent", clickedEvent );
        startActivity(eventDetailsIntent);
    }

    @Override
    public void onFavouriteClick(int position){

        Event event = myFavourites.get(position);
        String eventId = event.getEventID();
        System.out.println("Favourite!");

        mDatabaseRef.child("events").child(eventId).child("favourites").child(FirebaseAuth.getInstance().getUid()).setValue(FirebaseAuth.getInstance().getUid());
        mDatabaseRef.child("user").child(FirebaseAuth.getInstance().getUid()).child("favourites").child(eventId).setValue(eventId);
    }

    @Override
    public void onUnFavouriteClick(int position){

        Event event = myFavourites.get(position);
        String eventId = event.getEventID();
        System.out.println("UnFavourite!!!");

        mDatabaseRef.child("events").child(eventId).child("favourites").child(FirebaseAuth.getInstance().getUid()).removeValue();
        mDatabaseRef.child("events").child(FirebaseAuth.getInstance().getUid()).child("favourites").child(eventId).removeValue();
    }
}
