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

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.List.EventInfoActivity;
import com.example.marcu.androidros.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyFavouritesFragment extends Fragment implements MyPageAdapter.OnEventClickListener {

    private String TAG = "MyFavouritesFragment";
    private RecyclerView recyclerView;
    private MyPageAdapter myAdapter;
    private ArrayList<Event> allEvents = new ArrayList<>();
    private ArrayList<Event> myFavourites = new ArrayList<>();
    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.top_fragment_recycler);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();


        database.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (firebaseUser != null) {
                    String id = firebaseUser.getUid();
                    //User user = dataSnapshot.child(id).getValue(User.class);


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
                        if (allEvents.get(i) != null) {
                            if (dataSnapshot.child(id).child("favourites").hasChild(allEvents.get(i).getEventID())) {
                                Event event = allEvents.get(i);
                                myFavourites.add(event);
                            }
                        }
                    }

                    myAdapter = new MyPageAdapter(getActivity(), myFavourites);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.setOnEventClickListener(MyFavouritesFragment.this);
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        mDatabaseRef.child("events").child(eventId).child("likes").setValue(event.getLikes() + 1);
        mDatabaseRef.child("users").child(FirebaseAuth.getInstance().getUid()).child("favourites").child(eventId).setValue(eventId);
        event.setLikes(event.getLikes()+1);
    }

    @Override
    public void onUnFavouriteClick(int position){

        Event event = myFavourites.get(position);
        String eventId = event.getEventID();
        System.out.println("UnFavourite!");

        mDatabaseRef.child("events").child(eventId).child("likes").setValue(event.getLikes() - 1);
        mDatabaseRef.child("users").child(FirebaseAuth.getInstance().getUid()).child("favourites").child(eventId).removeValue();
        event.setLikes(event.getLikes()-1);
    }
}
