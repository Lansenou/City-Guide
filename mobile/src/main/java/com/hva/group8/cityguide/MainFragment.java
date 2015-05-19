package com.hva.group8.cityguide;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.viewpagerindicator.TabPageIndicator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MainFragment extends Fragment {
    private ViewPager mPager;
    private TabPageIndicator mTabPageIndicator;
    private TabPageIndicatorAdapter mTabPageIndicatorAdapter;
    private View view;
    private static MainFragment instance = null;

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
        return mTabPageIndicatorAdapter;
    }

    public ViewPager getPager() {
        return mPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mTabPageIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        mTabPageIndicatorAdapter = new TabPageIndicatorAdapter(getFragmentManager());
        mPager.setAdapter(mTabPageIndicatorAdapter);
        mTabPageIndicator.setViewPager(mPager);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        instance = null;
        //mTabPageIndicatorAdapter.removeAll();
    }

}
