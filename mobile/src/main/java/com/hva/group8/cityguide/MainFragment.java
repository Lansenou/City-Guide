package com.hva.group8.cityguide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;

public class MainFragment extends Fragment {

    public static MainFragment instance;
    private ViewPager mPager;
    private TabPageIndicator mTabPageIndicator;
    private TabPageIndicatorAdapter mTabPageIndicatorAdapter;

    public static MainFragment getInstance() {
        if (instance == null)
            instance = new MainFragment();
        return instance;
    }

    public static MainFragment newInstance() {
        return (instance = new MainFragment());
    }

    public TabPageIndicatorAdapter getTabPageIndicatorAdapter() {
        if (mTabPageIndicatorAdapter == null) {
            mTabPageIndicatorAdapter = new TabPageIndicatorAdapter(getFragmentManager());
        }
        if (mTabPageIndicator == null)
            Log.e("Main Fragment", "mTabPageIndicator was null");
        return mTabPageIndicatorAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("Created Main", "Fragment");
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference from the layout
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mTabPageIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);

        // Attach adapter to ViewPager
        mPager.setAdapter(getTabPageIndicatorAdapter());

        // Attach ViewPager to the indicator
        mTabPageIndicator.setViewPager(mPager, 0);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        instance = null;
        //mTabPageIndicatorAdapter.removeAll();
    }

    public boolean onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            //Home Tab

            CustomFragment currentFragment = ((CustomFragment) getTabPageIndicatorAdapter().getItem(0));
            Fragment parent = currentFragment.parent;
            if (parent != null) {
                //Set it to parent if it has one
                ((MainActivity) getActivity()).SwitchFragment(parent, false, 0);
                return true;
            }
            //Check if it fragment is home
            if (!getTabPageIndicatorAdapter().getItem(0).toString().contains("HomeFragment"))
                ((MainActivity) getActivity()).SwitchFragment(HomeFragment.newInstance(), false, 0);
            //Close Activity
            else getActivity().finish();

        } else if (mPager.getCurrentItem() == 1) {
            //Route Tab
            //Check if it fragment is route
            if (!getTabPageIndicatorAdapter().getItem(1).toString().contains("RouteFragment"))
                ((MainActivity) getActivity()).SwitchFragment(RouteFragment.newInstance(), false, 1);
            //Set it to Tab to Home
            else mPager.setCurrentItem(0);
            return true;
        } else if (mPager.getCurrentItem() == 2) {
            mPager.setCurrentItem(1);
            return true;
        }
        return false;
    }
}
