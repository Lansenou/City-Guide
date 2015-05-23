/*
 * Copyright (c) 2015.
 * Created by Lansenou on 23/05/15 21:05
 *
 */

package com.hva.group8.cityguide;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.hva.group8.cityguide.Loaders.LoadActivityItemFromURL;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by Lansenou on 23/05/2015.
 * Used in City-Guide
 */
public class SearchActivityFragment extends CustomFragment {

    //Add views to this list to show/hide them
    public ArrayList<View> viewList = new ArrayList<>();
    //The standard search and listview in each activity
    public SearchView searchView;
    public ListView listView;
    //Async loader
    private LoadActivityItemFromURL async = null;
    private ArrayList<ActivityItem> itemList;

    public void setupSearch(View view) {
        //Get Views
        listView = (ListView) view.findViewById(R.id.searchListView);
        searchView = (SearchView) view.findViewById(R.id.searchView);

        //Get the close button ID and add a close listener
        int closeButtonId = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) searchView.findViewById(closeButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearchView();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Make sure not to run when the query is empty
                if (query.isEmpty()) {
                    if (async != null)
                        async.CancelTask(true);
                    return false;
                }

                //Prepare the query with a name valuepair
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("query", query));

                //Instantiate ItemList and JSONAdapter
                itemList = new ArrayList<>();
                ActivityListAdapter adapter = new ActivityListAdapter(getActivity().getApplicationContext(), 0, itemList);

                //Async loads item into the adapter
                if (async != null)
                    async.cancel(true);
                async = new LoadActivityItemFromURL(adapter, nameValuePairs, getActivity().getApplicationContext(), "http://www.lansenou.com/database/search.php");
                async.dialog(getActivity());
                async.execute();

                //Set the listview it's adapter
                listView.setAdapter(adapter);

                //Set OnClickListener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ViewActivityFragment fragment = ViewActivityFragment.newInstance();
                        fragment.myItem = itemList.get(position);
                        ((MainActivity) getActivity()).SwitchFragment(fragment, false, 0);
                    }
                });

                //Remove keyboard pop up
                searchView.clearFocus();

                //Hide views
                for (int i = 0; i < viewList.size(); i++) {
                    viewList.get(i).setVisibility(View.GONE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });
    }

    public void closeSearchView() {
        //Show all views
        for (int i = 0; i < viewList.size(); i++) {
            viewList.get(i).setVisibility(View.VISIBLE);
        }

        //Close Searchview and listview
        searchView.setQuery("", false);
        searchView.clearFocus();

        //Stop async task
        if (async != null)
            async.CancelTask(true);
    }
}
