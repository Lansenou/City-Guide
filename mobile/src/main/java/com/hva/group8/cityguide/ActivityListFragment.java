package com.hva.group8.cityguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hva.group8.cityguide.Loaders.LoadActivityItemFromURL;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class ActivityListFragment extends Fragment {

    private static ActivityListFragment instance;
    HomeGroupItem info;
    String tableName;
    List<ActivityItem> itemList = new ArrayList<>();

    public static ActivityListFragment getInstance() {
        if (instance == null)
            instance = new ActivityListFragment();
        return instance;
    }

    public static ActivityListFragment newInstance() {
        return (instance = new ActivityListFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setDividerHeight(2);

        //Name value pairs
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("tableName", tableName));
        nameValuePairs.add(new BasicNameValuePair("query", info.Query));
        Log.e("Query", info.Query + ", Tablename: " + tableName);

        //Add adapter
        ActivityListAdapter adapter = new ActivityListAdapter(getActivity().getApplicationContext(), 0, itemList);
        LoadActivityItemFromURL async = new LoadActivityItemFromURL(adapter, nameValuePairs, getActivity().getApplicationContext(), "http://www.lansenou.com/database/ophalen.php");
        async.dialog(getActivity()).execute();

        //Set adapter to our list
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //putExtra("activity", itemList.get(position));
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        itemList.clear();
        //mTabPageIndicatorAdapter.removeAll();
    }
}
