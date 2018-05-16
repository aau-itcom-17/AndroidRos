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
import android.widget.Button;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.EventPopUp;

import java.util.ArrayList;


public class TopFragment extends Fragment implements View.OnClickListener{
    String TAG = "TopFragment";
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Event> events;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.top_fragment_recycler);

        if(getArguments() != null) {
            events = getArguments().getParcelableArrayList("key");

            for (int i = 0; i < events.size(); i++) {
                Log.i(TAG, events.get(i).getName());
            }
        }else{
            Log.i(TAG, "getArguments = null");
        }


        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new MainAdapter(getContext(), events);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);



        recyclerView.setOnClickListener(this);

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
    public void onClick(View v) {
        switch (v.getId()) {
            //case R.id.event_button:
             //   startActivity(new Intent(getActivity(), EventPopUp.class));
             //   break;
        }
    }

}