package com.example.marcu.androidros.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.EventPopUp;

public class TopFragment extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        Button eventButton = (Button) view.findViewById(R.id.event_button);
        eventButton.setOnClickListener(this);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.likeIcon);
        int count = 1;

        if (checkBox.isSelected() == true) {
            count++;
            TextView textView= (TextView) view.findViewById(R.id.likesCount);
            try {
                textView.setText(""+count);

            } catch (Exception e) {
                System.out.println("Exception occurred");
            }
        } else {
            count=0;
            TextView textView= (TextView) view.findViewById(R.id.likesCount);
            try {
                textView.setText(""+count);

            } catch (Exception e) {
                System.out.println("Exception occurred");
            }
        }



        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_button:
                startActivity(new Intent(getActivity(), EventPopUp.class));
                break;
        }


    }





}