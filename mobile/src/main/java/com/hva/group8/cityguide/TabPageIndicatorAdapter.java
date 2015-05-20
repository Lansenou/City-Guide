package com.hva.group8.cityguide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Mustafa on 12-5-2015.
 */

public class TabPageIndicatorAdapter extends FragmentStatePagerAdapter {
    private static final String[] tabTitles = new String[] {"Home", "Route", "Start"};
    private Fragment homeFragment;
    private Map<Integer,Fragment> fragmentMap = new HashMap<Integer,Fragment>();

    public TabPageIndicatorAdapter(FragmentManager fm) {
        super(fm);
    }

    public void replaceHomeFragment(Fragment fragment) {
        homeFragment = fragment;
        //notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance();
                }
                fragment = homeFragment;
                break;
            default:
                fragment = BlankFragment.newInstance();
                break;
        }
        fragmentMap.put(Integer.valueOf(position), fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        fragmentMap.remove(Integer.valueOf(position));
    }

    public void removeAll() {
        Iterator iterator = fragmentMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            Fragment fragment = (Fragment) entry.getValue();
            fragment.onDestroyView();//Trigger inner fragment's DestroyView() method !
        }
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
