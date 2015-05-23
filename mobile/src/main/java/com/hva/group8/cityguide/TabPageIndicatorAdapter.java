package com.hva.group8.cityguide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

/**
 * Created by Mustafa on 12-5-2015.
 */

public class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {

    //Tab pages
    private static final String[] tabTitles = new String[]{"Home", "Route", "Start"};

    private FragmentManager fm;

    //Constructor
    public TabPageIndicatorAdapter(FragmentManager fm, MainActivity parent) {
        super(fm);
        this.fm = fm;
    }

    //Fragment
    private Fragment homeFragment;

    //Fragment
    private Fragment routeFragment;

    public void ReplaceHomeFragment(Fragment fragment) {
        homeFragment = fragment;
        notifyDataSetChanged();
    }

    public void ReplaceRouteFragment(Fragment fragment) {
        routeFragment = fragment;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                if (homeFragment == null)
                    homeFragment = HomeFragment.newInstance();
                fragment = homeFragment;
                break;
            case 1:
                if (routeFragment == null)
                    routeFragment = RouteFragment.newInstance();
                fragment = routeFragment;
                break;
            default:
                fragment = BlankFragment.newInstance();
        }
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position % tabTitles.length];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
