package com.example.marcu.androidros.Favourites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.R;

import java.util.List;

public class MyEventFragment extends Fragment {


    public static List<Event> events, myEvents, newestEvents, byNameEvents;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_event, container, false);
        return view;
    }

    events = new ArrayList<>();
    myEvents = new ArrayList<>();
    newestEvents = new ArrayList<>();
    byNameEvents = new ArrayList<>();

    myEvents = new ArrayList<>();
    ArrayAdapter<myEvents> adaptor;


    adaptor = new ArrayAdapter<String>(this, R.layout.list_item, myEvents);

    setListAdapter(adaptor);


}
