package com.example.marcu.androidros.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.EventPopUp;

public class TopFragment extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);

        Button eventButton = (Button) view.findViewById(R.id.event_button);
        eventButton.setOnClickListener(this);

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