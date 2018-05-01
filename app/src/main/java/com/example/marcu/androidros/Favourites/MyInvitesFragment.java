package com.example.marcu.androidros.Favourites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.R;

import java.util.ArrayList;
import java.util.List;

public class MyInvitesFragment extends Fragment {

    private static User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_invites, container, false);

        String[] invites = new String[user.getInvites().size()];
        List listOfInvites = new ArrayList();

        if (user.getInvites().size() == 0)
        {
            listOfInvites.add("No Invites");
        }
        else
        {
            for (int i = 0; i < user.getInvites().size(); i++) {
                listOfInvites.add(user.getInvites().get(i).getName());
            }
        }
        listOfInvites.toArray(invites);




        ListView listView = (ListView) view.findViewById(R.id.invitesList);

        ArrayAdapter<String> listViewAdaptor = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, listOfInvites
        );

        listView.setAdapter(listViewAdaptor);

        return view;
    }
}
