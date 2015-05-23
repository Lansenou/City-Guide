package com.hva.group8.cityguide;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends SearchActivityFragment {

    public static HomeFragment instance;

    public static HomeFragment getInstance() {
        if (instance == null)
            instance = new HomeFragment();
        return instance;
    }

    public static HomeFragment newInstance() {
        return (instance = new HomeFragment());
    }

    ExpandableListView mExpendableListView;
    List<HomeGroupItem> groupItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Setup ExpandableListView View
        mExpendableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        mExpendableListView.setDividerHeight(2);
        mExpendableListView.setGroupIndicator(null);
        mExpendableListView.setClickable(true);
        setGroupData();

        //Create Adapter
        CustomHomeListAdapter homeListAdapter = new CustomHomeListAdapter(groupItems, mExpendableListView,
                (MainActivity) getActivity(),
                (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        //Set adapter
        mExpendableListView.setAdapter(homeListAdapter);

        //Setup SearchActivityFragment
        setupSearch(view);
        viewList.add(mExpendableListView);
        return view;
    }

    public void setGroupData() {
        List<HomeGroupItem> a = new ArrayList<>();
        List<HomeGroupItem> b = new ArrayList<>();
        groupItems.add(new HomeGroupItem(R.drawable.categories, "Categories"));
        a.add(new HomeGroupItem(R.drawable.no_image, "test2"));
        groupItems.add(new HomeGroupItem(R.drawable.cat_top10, "Top 10 locations", a));
        b.add(new HomeGroupItem(R.drawable.no_image, "test3"));
        groupItems.add(new HomeGroupItem(R.drawable.cat_nearby, "Nearby locations", b));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        groupItems.clear();
    }
}
