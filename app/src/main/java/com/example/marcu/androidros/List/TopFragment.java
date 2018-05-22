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
import android.widget.Toast;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.EventPopUp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;


public class TopFragment extends Fragment implements MainAdapter.OnEventClickListener {
    FirebaseDatabase database;
    String TAG = "TopFragment";
    RecyclerView recyclerView;
    MainAdapter adapter ;
    Query events;
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
            //events = getArguments().getParcelableArrayList("key");
            events = (Query) mDatabaseRef.child("events").child("").orderByChild("likes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        System.out.println(child.getKey());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
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
    public void onEventClick(int position) {
        Intent eventDetailsIntent = new Intent(getActivity(), EventInfoActivity.class);


        startActivity(eventDetailsIntent);
    }

    @Override
    public void onFavouriteClick(int position){



        System.out.println("Favourite!");



        /*database = FirebaseDatabase.getInstance();
        database.getReference("events").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                long numOfLikes = dataSnapshot.child("events").child(id).child("favourites").getChildrenCount();
                mDatabaseRef.child("events").child(id).child("favourites").child("favourites").child(Long.toString(numOfLikes + 1)).setValue(FirebaseAuth.getInstance().getUid());

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/




    }

    @Override
    public void onUnFavouriteClick(int position){
        System.out.println("UnFavourite!!!");

        /*database = FirebaseDatabase.getInstance();
        database.getReference("events").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                long numOfLikes = dataSnapshot.child("events").child(id).child("favourites").getChildrenCount();
                mDatabaseRef.child("events").child(id).child("favourites").child("favourites").child(Long.toString(numOfLikes + 1)).setValue(FirebaseAuth.getInstance().getUid());

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/



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