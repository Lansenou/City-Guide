/*
 * Copyright (c) 2015.
 * Created by Lansenou on 19-5-2015.
 *
 */

package com.hva.group8.cityguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class CategoryFragment extends SearchActivityFragment {

    public static CategoryFragment instance;

    public static CategoryFragment getInstance() {
        if (instance == null)
            instance = new CategoryFragment();
        return instance;
    }

    public static CategoryFragment newInstance() {
        return (instance = new CategoryFragment());
    }

    private List<HomeGroupItem> itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setDividerHeight(2);

        //Load List
        fillCategoryList();

        //Load Adapter with list
        CustomCategoryAdapter adapter = new CustomCategoryAdapter(getActivity().getApplicationContext(), R.layout.group_row, itemList);

        //Set adapter to our list
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity().getApplicationContext(), itemList.get(position).Title, Toast.LENGTH_SHORT).show();
                SubCategoryFragment fragment = SubCategoryFragment.newInstance();
                fragment.number = position;
                fragment.parent = getInstance();
                ((MainActivity) getActivity()).SwitchFragment(fragment, false, 0);
            }
        });

        setupSearch(view);
        viewList.add(listView);
        return view;
    }

    private void fillCategoryList() {
        itemList = new ArrayList<>();
        itemList.add(new HomeGroupItem(R.drawable.cat_activities, getString(R.string.cat_activities)));
        itemList.add(new HomeGroupItem(R.drawable.cat_attractions, getString(R.string.cat_attractions)));
        itemList.add(new HomeGroupItem(R.drawable.cat_food_drinks, getString(R.string.cat_foodandrinks)));
        itemList.add(new HomeGroupItem(R.drawable.sub_gallery_museum, getString(R.string.cat_museum)));
        itemList.add(new HomeGroupItem(R.drawable.cat_theatre, getString(R.string.sub_theater)));
        itemList.add(new HomeGroupItem(R.drawable.cat_nightlife, getString(R.string.cat_night)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        instance = null;
        //mTabPageIndicatorAdapter.removeAll();
    }
}
