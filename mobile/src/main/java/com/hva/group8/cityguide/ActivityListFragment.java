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


public class ActivityListFragment extends CustomFragment {

    public static ActivityListFragment instance;

    public static ActivityListFragment getInstance() {
        if (instance == null)
            instance = new ActivityListFragment();
        return instance;
    }

    public static ActivityListFragment newInstance() {
        return (instance = new ActivityListFragment());
    }

    HomeGroupItem info;
    String tableName;
    List<ActivityItem> itemList;
    ListView listView;

    private boolean fragmentResume=false;
    private boolean fragmentVisible=false;
    private boolean fragmentOnCreated=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setDividerHeight(2);

        if (!fragmentResume && fragmentVisible)
            updateList();
        else {
            //Add adapter
            ActivityListAdapter adapter = new ActivityListAdapter(getActivity().getApplicationContext(), 0, itemList);

            //Set adapter to our list
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //putExtra("activity", itemList.get(position));
                    ViewActivityFragment fragment = ViewActivityFragment.newInstance();
                    fragment.myItem = itemList.get(position);
                    ((MainActivity) getActivity()).SwitchFragment(fragment, false, 0);
                }
            });
        }
        return view;
    }

    private void updateList() {
        //Name value pairs
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("tableName", tableName));
        nameValuePairs.add(new BasicNameValuePair("query", info.Query));
        Log.e("Query", info.Query + ", Tablename: " + tableName);

        itemList = new ArrayList<>();

        //Add adapter
        ActivityListAdapter adapter = new ActivityListAdapter(getActivity().getApplicationContext(), 0, itemList);

        //Load items into the list
        LoadActivityItemFromURL async = new LoadActivityItemFromURL(adapter, nameValuePairs, getActivity().getApplicationContext(), "http://www.lansenou.com/database/ophalen.php");
        async.dialog(getActivity()).execute();

        //Set adapter to our list
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //putExtra("activity", itemList.get(position));
                ViewActivityFragment fragment = ViewActivityFragment.getInstance();
                fragment.myItem = itemList.get(position);
                fragment.parent = getInstance();
                ((MainActivity) getActivity()).SwitchFragment(fragment, false, 0);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean visible){
        super.setUserVisibleHint(visible);
        if (visible && isResumed()){   // only at fragment screen is resumed
            fragmentResume=true;
            fragmentVisible=false;
            fragmentOnCreated=true;
            updateList();
        }else  if (visible){        // only at fragment onCreated
            fragmentResume=false;
            fragmentVisible=true;
            fragmentOnCreated=true;
        }
        else if(!visible && fragmentOnCreated){// only when you go out of fragment screen
            fragmentVisible=false;
            fragmentResume=false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        itemList.clear();
    }
}
