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

import java.util.ArrayList;
import java.util.List;

public class MyFavouritesFragment extends Fragment {


    //public static List<Event> events, myEvents, newestEvents, byNameEvents;
    public static List<Event> events = new ArrayList<>();
    public static List<Event> myEvents = new ArrayList<>();
    public static List<Event> newestEvents = new ArrayList<>();
    public static List<Event> byNameEvents = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_favourites, container, false);
        return view;

        /*ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textView, myEvents);
        myEvents.setAdapter(arrayAdapter);*/
    }


    /*

    ArrayAdapter<myEvents> adaptor;

    adaptor = new ArrayAdapter<String>(this, R.layout.list_item, myEvents);

    setListAdapter(adaptor);
*/
}
