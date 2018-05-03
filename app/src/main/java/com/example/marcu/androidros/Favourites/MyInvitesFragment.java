package com.example.marcu.androidros.Favourites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.Map.MapActivity;
import com.example.marcu.androidros.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyInvitesFragment extends Fragment {

    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    public static String[] invites;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_invites, container, false);

        database = FirebaseDatabase.getInstance();
        database.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {


            //Get the user logged in, from the database:
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = firebaseUser.getUid();
                Log.i("Firebase", "uid: " + uid);
                User user = dataSnapshot.child(uid).getValue(User.class);


                //Create a list for invites:
                List listOfInvites = new ArrayList();


                //Write "No current invites", if there is nothing in invites:
                if (user.getInvites() == null) {

                    System.out.println("null");

                    invites = new String[1];
                    listOfInvites.add("No current invites");
                    listOfInvites.toArray(invites);


                //Adds all invites to the list:
                } else {

                    System.out.println("not null");

                    invites = new String[user.getFavourites().size()];

                    for (int i = 0; i < user.getInvites().size(); i++)
                    {
                        listOfInvites.add(user.getInvites().get(i).getName());
                    }
                    listOfInvites.toArray(invites);
                }


                //Shows the invites:
                ListView listView = (ListView) view.findViewById(R.id.invitesList);

                ArrayAdapter<String> listViewAdaptor = new ArrayAdapter<String>(
                        getActivity(), android.R.layout.simple_list_item_1, listOfInvites
                );

                listView.setAdapter(listViewAdaptor);


                //Action when clicking on the event:

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
        return view;
    }
}
