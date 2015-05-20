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
    private static MainFragment instance = null;
    private ViewPager mPager;
    private TabPageIndicator mTabPageIndicator;
    private TabPageIndicatorAdapter mTabPageIndicatorAdapter;
    private View view;

    public MainFragment() {
    }

    public static MainFragment getInstance() {
        if (instance == null) {
            instance = new MainFragment();
        }
        return instance;
    }

    public static MainFragment newInstance() {
        instance = new MainFragment();
        return instance;
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
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_main, container, false);

        //Get from
        mTabPageIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        mPager = (ViewPager) view.findViewById(R.id.pager);

        //Needs to be in this order to make sure the pager has an adapter
        mPager.setAdapter(getTabPageIndicatorAdapter());
        mTabPageIndicator.setViewPager(mPager);

        Log.e("Adapter is null", (mTabPageIndicator == null) + "");
        Log.e("idk", "test");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        instance = null;
        //mTabPageIndicatorAdapter.removeAll();
    }
}
