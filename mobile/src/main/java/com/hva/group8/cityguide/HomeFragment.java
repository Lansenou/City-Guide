package com.hva.group8.cityguide;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    ExpandableListView mExpendableListView;
    List<HomeGroupItem> groupItems = new ArrayList<HomeGroupItem>();

    public static HomeFragment newInstance() {
        HomeFragment f = new HomeFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //TODO Style SearchView

        mExpendableListView = (ExpandableListView)view.findViewById(R.id.expandableListView);
        mExpendableListView.setDividerHeight(2);
        mExpendableListView.setGroupIndicator(null);
        mExpendableListView.setClickable(true);
        setGroupData();

        CustomHomeListAdapter homeListAdapter = new CustomHomeListAdapter(groupItems, mExpendableListView);
        homeListAdapter
                .setInflater(
                        (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE),
                        this.getActivity());

        mExpendableListView.setAdapter(homeListAdapter);
        return view;
    }

    public void setGroupData() {
        List<HomeGroupItem> a = new ArrayList<HomeGroupItem>();
        List<HomeGroupItem> b = new ArrayList<HomeGroupItem>();
        groupItems.add(new HomeGroupItem(R.drawable.categories, "Categories"));
        a.add(new HomeGroupItem(R.drawable.ic_launcher, "test2"));
        groupItems.add(new HomeGroupItem(R.drawable.top_locations, "Top 10 locations", a));
        b.add(new HomeGroupItem(R.drawable.ic_launcher, "test3"));
        groupItems.add(new HomeGroupItem(R.drawable.nearby_locations, "Nearby locations", b));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        groupItems.clear();
        //mTabPageIndicatorAdapter.removeAll();
    }
//On viewdestroy delete listview data ofs?
//    @Override
//    public boolean onChildClick(ExpandableListView parent, View v,
//                                int groupPosition, int childPosition, long id) {
//        Toast.makeText(getActivity(), "Clicked On Child",
//                Toast.LENGTH_SHORT).show();
//        return true;
//    }

}
