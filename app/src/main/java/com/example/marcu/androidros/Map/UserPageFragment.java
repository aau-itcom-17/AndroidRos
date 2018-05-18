package com.example.marcu.androidros.Map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.Database.User;
import com.example.marcu.androidros.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class UserPageFragment extends Fragment implements View.OnClickListener {

    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private ArrayList<Event> events = new ArrayList<>();
    private List<String> eventIDs = new ArrayList<>();


    //TextView firstName = (TextView) findViewById(R.id.getFirstName);



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userpage, container, false);

        ImageView closeButton = (ImageView) view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(this);

        final TextView fName = (TextView) view.findViewById(R.id.getFirstName);
        final TextView lName = (TextView) view.findViewById(R.id.getLastName);
        final TextView email = (TextView) view.findViewById(R.id.getEmail);


        // Access data to firebase
        database = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // Get the user that is logged in
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = firebaseUser.getUid(); //Get the ID of the user that is logged in
                User user = dataSnapshot.child(uid).getValue(User.class); // Retrieving the user's information from the database
                fName.setText(user.getFirstName());
                lName.setText(user.getLastName());
                email.setText(user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        return view;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_button:
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
        }

    }
}