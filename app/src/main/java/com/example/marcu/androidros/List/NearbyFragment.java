package com.example.marcu.androidros.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.marcu.androidros.Create.CreateActivity;
import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.Database.EventList;
import com.example.marcu.androidros.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NearbyFragment extends Fragment {

    private DatabaseReference mDataBaseReference;
    private ListView mListViewNearby;

    List<Event> mEventList = new ArrayList<Event>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);


        mDataBaseReference = FirebaseDatabase.getInstance().getReference("events");

        mListViewNearby = (ListView) view.findViewById(R.id.nearby_listview);

        mDataBaseReference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                mEventList.clear();

                for(DataSnapshot eventSnapshot : dataSnapshot.getChildren()){
                    Event event = eventSnapshot.getValue(Event.class);

                    mEventList.add(event);
                }

                EventList adapter = new EventList(getActivity(), R.layout.list_layout, mEventList);

                //NearbyFragment.this, mEventList
                mListViewNearby.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;

    }

}