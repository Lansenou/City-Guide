package com.hva.group8.cityguide;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hva.group8.cityguide.Loaders.LoadActivityItemFromURL;
import com.hva.group8.cityguide.Managers.UserInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ActivityListFragment extends SearchActivityFragment {

    public static ActivityListFragment instance;
    //Other fragment sets these
    public HomeGroupItem info;
    public String tableName;
    //Used to create listview
    private List<ActivityItem> itemList;
    private ListView listView;
    private ActivityListAdapter adapter;
    //Prevent from reloading every time the user click on a different tab
    private boolean fragmentResume=false;
    private boolean fragmentVisible=false;
    private boolean fragmentOnCreated=false;

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
        View view = inflater.inflate(R.layout.fragment_activitylist, container, false);
        listView = (ListView) view.findViewById(R.id.listView);

        //Prevent from reloading every time the user click on a different tab
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

        //Todo make this tabs

        //Get group from view
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        //Get which item was checked, send that to sortlist
        radioGroup.check(0);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedId);
                int checkedIndex = radioGroup.indexOfChild(checkedRadioButton);
                sortList(checkedIndex);
                Log.i("Index was", checkedIndex + ".");
            }
        });


        return view;
    }

    private void sortList(int sortMethod) {
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

            case 2:     //ToDo Sort list by rating
                Collections.sort(itemList, new Comparator<ActivityItem>() {
                    public int compare(ActivityItem o1, ActivityItem o2) {
                        if (o1.Distance == o2.Distance)
                            return 0;
                        return (o1.Distance > o2.Distance) ? 1 : -1;
                    }
                });
                break;

            default:
                break;
        }
        //Notify adapter
        adapter.notifyDataSetChanged();
    }

    private void updateList() {
        //Name value pairs
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("tableName", tableName));
        nameValuePairs.add(new BasicNameValuePair("query", info.Query));
        Log.e("Query", info.Query + ", Tablename: " + tableName);

        itemList = new ArrayList<>();

        //Add adapter
        adapter = new ActivityListAdapter(getActivity().getApplicationContext(), 0, itemList);

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
