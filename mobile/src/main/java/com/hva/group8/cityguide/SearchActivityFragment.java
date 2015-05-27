/*
 * Copyright (c) 2015.
 * Created by Lansenou on 23/05/15 21:05
 *
 */

package com.hva.group8.cityguide;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.hva.group8.cityguide.Loaders.LoadActivityItemFromURL;
import com.hva.group8.cityguide.Managers.UserInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    public ArrayList<ActivityItem> searchItemList;
    public ActivityListAdapter searchAdapter;
    //Async loader
    private LoadActivityItemFromURL async = null;

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
                searchItemList = new ArrayList<>();
                searchAdapter = new ActivityListAdapter(getActivity().getApplicationContext(), 0, searchItemList);

                //Async loads item into the adapter
                if (async != null)
                    async.cancel(true);
                async = new LoadActivityItemFromURL(searchAdapter, nameValuePairs, getActivity().getApplicationContext(), "http://www.lansenou.com/database/search.php");
                async.dialog(getActivity());
                async.execute();

                //Set the listview it's adapter
                listView.setAdapter(searchAdapter);

                //Set OnClickListener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ViewActivityFragment fragment = ViewActivityFragment.newInstance();
                        fragment.myItem = searchItemList.get(position);
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

    public void sortList(int sortMethod, List<ActivityItem> itemList, ArrayAdapter adapter) {
        if (itemList == null || adapter == null)
            return;
        switch (sortMethod) {
            case 0:     //Sort list by name
                Collections.sort(itemList, new Comparator<ActivityItem>() {
                    public int compare(ActivityItem o1, ActivityItem o2) {
                        String title1 = UserInfo.getInstance().getLanguage().equals("nl") ? o1.Title : o1.TitleEN;
                        String title2 = UserInfo.getInstance().getLanguage().equals("nl") ? o2.Title : o2.TitleEN;
                        return title1.compareToIgnoreCase(title2);
                    }
                });

                break;

            case 1:     //Sort list by distance
                Collections.sort(itemList, new Comparator<ActivityItem>() {
                    public int compare(ActivityItem o1, ActivityItem o2) {
                        if (o1.Distance == o2.Distance)
                            return 0;
                        return (o1.Distance > o2.Distance) ? 1 : -1;
                    }
                });

                break;

            case 2:     // Sort list by rating
                Collections.sort(itemList, new Comparator<ActivityItem>() {
                    public int compare(ActivityItem o1, ActivityItem o2) {
                        int totalLikes1 = o1.Likes + o1.Dislikes;
                        int totalLikes2 = o2.Likes + o2.Dislikes;
                        float Rating1 = (float) o1.Likes / ((float) totalLikes1);
                        float Rating2 = (float) o2.Likes / ((float) totalLikes2);


                        //Dividing by zero
                        if (Float.isNaN(Rating1)) {
                            Rating1 = 0;
                        }
                        if (Float.isNaN(Rating2)) {
                            Rating2 = 0;
                        }

                        Log.e("Rating", Rating1 + ".");
                        if (Rating1 == Rating2) {
                            if (totalLikes1 == totalLikes2)
                                return 0;
                            return totalLikes1 > totalLikes2 ? 1 : -1;
                        }
                        return (Rating1 > Rating2) ? 1 : -1;
                    }
                });
                Collections.reverse(itemList);
                break;

            default:
                break;
        }
        //Notify adapter
        adapter.notifyDataSetChanged();
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
