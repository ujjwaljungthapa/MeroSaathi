package com.electronics.invento.merosaathi;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class SearchViewTaxiActivity extends AppCompatActivity {

    String inital_name, final_name;
    Float total_time, total_distance;
    Bundle gotBasket;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view_bus);

        gotBasket = getIntent().getExtras();
        inital_name = gotBasket.getString("initial");
        final_name = gotBasket.getString("final");
        total_time = gotBasket.getFloat("avgTime");
        total_distance = gotBasket.getFloat("avgDistance");

        initializeComponents();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        // Create an adapter that knows which fragment should be shown on each page
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        // Find the view pager that will allow the user to swipe between fragments
        mViewPager = findViewById(R.id.container);
        // Set the adapter onto the view pager
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
               /* if(tab.getPosition() == 0){
                    tab.setIcon(R.drawable.icon_selected_access_time_black_24dp);
                }else if (tab.getPosition() == 1){
                    tab.setIcon(R.drawable.icon_selected_public_black_24dp);
                }else if (tab.getPosition() == 2){
                    tab.setIcon(R.drawable.icon_selected_payment_black_24dp);
                }*/
                //tab.getIcon().setBounds(5,5,100,100);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initializeComponents() {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
/*
    public static class PlaceholderFragment extends Fragment {
        */
/**
 * The fragment argument representing the section number for this
 * fragment.
 *//*

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        */
/**
 * Returns a new instance of this fragment for the given section
 * number.
 *//*

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_bus_time
                    , container, false);
            */
/*TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*//*

            return rootView;
        }
    }
*/

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new BusTimeFragment();
                    fragment.setArguments(gotBasket);
                    break;
                case 1:
                    fragment = new BusDistanceFragment();
                    fragment.setArguments(gotBasket);
                    break;
                case 2:
                    fragment = new BusCostFragment();
                    fragment.setArguments(gotBasket);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
