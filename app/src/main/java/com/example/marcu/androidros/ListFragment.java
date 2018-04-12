package com.example.marcu.androidros;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

public class ListFragment extends Fragment implements View.OnClickListener {


    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        Button eventButton = (Button) view.findViewById(R.id.event_button);
        eventButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_button:
                startActivity(new Intent(getActivity(), Event.class));
                toolbar = (Toolbar)findViewById(R.id.toolBar);
                tabLayout = (TabLayout)findViewById(R.id.tabLayout);
                viewPager = (ViewPager)findViewById(R.id.viewPager);
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                viewPagerAdapter.addFragments(new TopEventsFragment(),"Top");
                viewPagerAdapter.addFragments(new NearbyEventsFragment(), "Nearby");
//                    viewPagerAdapter.addFragments(new NearbyEventsFragment(), "Recommended");
                viewPagerAdapter.addFragments(new NewEventsFragment(), "New");
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
                break;
        }
    }


    public FragmentManager getSupportFragmentManager() {
        return supportFragmentManager;
    }
}