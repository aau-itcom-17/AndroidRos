package com.example.marcu.androidros.Favourites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.R;

import java.util.ArrayList;
import java.util.List;

public class MyFavouritesFragment extends Fragment {


    //public static List<Event> events, myEvents, newestEvents, byNameEvents;
    private static User user;
    private static Event event;

    /*public static List<User> users;
    public static List<Event> myEvents = new ArrayList<>();
    public static List<Event> newestEvents = new ArrayList<>();
    public static List<Event> byNameEvents = new ArrayList<>();*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_favourites, container, false);

/*
        String[] favouriteItems = {"Kanye Camp", "My Camp", "Party"};
*/

        String[] favouriteEvents = new String[user.getFavourites().size()];
        List list = new ArrayList();


        if (user.getFavourites().size() == 0)
        {
            list.add("No Favourites");
        }
        else
        {
            for (int i = 0; i < user.getFavourites().size(); i++) {
                list.add(user.getFavourites().get(i).getName());
            }
        }
        list.toArray(favouriteEvents);





        ListView listView = (ListView) view.findViewById(R.id.favouritesList);

        ArrayAdapter<String> listViewAdaptor = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, favouriteEvents
        );

        listView.setAdapter(listViewAdaptor);

        return view;
    }
}
