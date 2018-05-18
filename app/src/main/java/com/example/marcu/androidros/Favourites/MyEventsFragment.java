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

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.List.EventInfoActivity;
import com.example.marcu.androidros.List.TopFragment;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.EventPopUp;

import java.util.ArrayList;

public class MyEventsFragment extends Fragment implements EventAdapter.OnEventClickListener{

    String TAG = "MyEventsFragment";
    RecyclerView recyclerView;
    EventAdapter myAdapter;
    ArrayList<Event> myEvents;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.my_events_fragment_recycler);

        if(getArguments() != null) {
            myEvents = getArguments().getParcelableArrayList("key");
            System.out.println("Second: " + myEvents.size());

            for (int i = 0; i < myEvents.size(); i++) {
                Log.i(TAG, myEvents.get(i).getName());
            }
        }else{
            Log.i(TAG, "getArguments = null");
        }


        myAdapter = new EventAdapter(getActivity(), myEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            //case R.id.event_button:
//            //   startActivity(new Intent(getActivity(), EventPopUp.class));
//            //   break;
//        }
//    }

}
