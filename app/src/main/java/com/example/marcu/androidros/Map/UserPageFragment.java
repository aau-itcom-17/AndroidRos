package com.example.marcu.androidros.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.marcu.androidros.R;
import com.example.marcu.androidros.Utils.EventPopUp;

public class UserPageFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userpage, container, false);

        ImageView closeButton = (ImageView) view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(this);

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
