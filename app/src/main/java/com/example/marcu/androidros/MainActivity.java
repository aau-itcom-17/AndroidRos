package com.example.marcu.androidros;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
/*    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
*/
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    selectedFragment = new MapFragment();
/*                    try {
                        viewPager.setAdapter(null);
                    } catch (Exception e) { //Need to find out the right exception

                    }*/
                    break;
                case R.id.navigation_list:
                    selectedFragment = new com.example.marcu.androidros.ListFragment();
/*                    toolbar = (Toolbar)findViewById(R.id.toolBar);
                    tabLayout = (TabLayout)findViewById(R.id.tabLayout);
                    viewPager = (ViewPager)findViewById(R.id.viewPager);
                    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                    viewPagerAdapter.addFragments(new TopEventsFragment(),"Top");
                    viewPagerAdapter.addFragments(new NearbyEventsFragment(), "Nearby");
//                    viewPagerAdapter.addFragments(new NearbyEventsFragment(), "Recommended");
                    viewPagerAdapter.addFragments(new NewEventsFragment(), "New");
                    viewPager.setAdapter(viewPagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);*/
                    break;
                case R.id.navigation_favourites:
                    selectedFragment = new FavouritesFragment();

                    break;

                case R.id.navigation_my_events:
                    selectedFragment = new CreateFragment();

                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
    }
}
