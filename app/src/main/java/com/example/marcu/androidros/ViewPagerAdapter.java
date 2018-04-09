package com.example.marcu.androidros;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tabTitles = new ArrayList<>();

    public void addFragments(Fragment fragments,String titles) {
        this.fragments.add(fragments);
        this.tabTitles.add(titles);
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
/*                   mTextMessage.setText(R.string.title_list);
                    toolbar = (Toolbar)findViewById(R.id.toolBar);
                    tabLayout = (TabLayout)findViewById(R.id.tabLayout);
                    viewPager = (ViewPager)findViewById(R.id.viewPager);
                    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                    viewPagerAdapter.addFragments(new TopEventsFragment(),"Top");
                    viewPagerAdapter.addFragments(new NearbyEventsFragment(), "Nearby");
                    viewPagerAdapter.addFragments(new NewEventsFragment(), "New");
                    viewPager.setAdapter(viewPagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);*/