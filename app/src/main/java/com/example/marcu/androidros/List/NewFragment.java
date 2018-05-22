package com.example.marcu.androidros.List;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NewFragment extends Fragment {

    String TAG = "NewFragment";
    FirebaseDatabase database;
    RecyclerView recyclerView;
    MainAdapter adapter ;
    ArrayList<Event> events;
    ImageButton favouriteButton;
    ImageButton unFavouriteButton;

    private DatabaseReference mDatabaseRef;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.top_fragment_recycler);
        favouriteButton = (ImageButton) view.findViewById(R.id.favourite_button);
        unFavouriteButton = (ImageButton) view.findViewById(R.id.favourite_button_clicked);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mDatabaseRef.child("events").orderByChild("date");



        if(getArguments() != null) {
            events = getArguments().getParcelableArrayList("key");
            for (int i = 0; i < events.size(); i++) {
                Log.i(TAG, events.get(i).getName());
            }
        }else{
            Log.i(TAG, "getArguments = null");
        }

        adapter = new MainAdapter(getActivity(), events);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnEventClickListener((MainAdapter.OnEventClickListener) NewFragment.this);

        return view;

    }
}