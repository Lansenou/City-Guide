package com.hva.group8.cityguide;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;


public class CategoryFragment extends Fragment {

    public static CategoryFragment newInstance() {
        CategoryFragment f = new CategoryFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ListView listView = (ListView)view.findViewById(R.id.listView);
        listView.setDividerHeight(2);
        //setGroupData();

        CustomHomeListAdapter homeListAdapter = new CustomHomeListAdapter(groupItems, mExpendableListView);
        homeListAdapter
                .setInflater(
                        (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE),
                        this.getActivity());

        mExpendableListView.setAdapter(homeListAdapter);
        return view;

        //textView.setText(getArguments().getString("text"));
        return view;
    }

}
