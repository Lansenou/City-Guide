/*
 * Copyright (c) 2015.
 * Created by Lansenou on 19-5-2015.
 *
 */

package com.hva.group8.cityguide;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hva.group8.cityguide.Managers.RouteManager;
import com.hva.group8.cityguide.Managers.UILManager;
import com.hva.group8.cityguide.Managers.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.List;


public class RouteFragment extends Fragment {

    private static RouteFragment instance;

    public static RouteFragment getInstance() {
        if (instance == null)
            instance = new RouteFragment();
        return instance;
    }

    public static RouteFragment newInstance() {
        return (instance = new RouteFragment());
    }

    //Managers
    RouteManager routeManager = RouteManager.getInstance();
    UILManager uilManager = UILManager.instance();
    UserInfo userInfo = UserInfo.getInstance();
    List<ActivityItem> routeList = routeManager.routeList;

    //Route Items
    ListView listView;
    RouteAdapter routeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route, container, false);

        //Set up list
        listView = (ListView) view.findViewById(R.id.routeListView);
        routeAdapter = new RouteAdapter(getActivity().getApplicationContext(), 0, routeList);
        listView.setAdapter(routeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Click", "Position:" + position);
                if (position > routeList.size())
                    ((MainActivity) getActivity()).SwitchFragment(CategoryFragment.newInstance(), false, 0);
            }
        });

        //Header and Footer
        loadHeaderAndFooter();

        //Register for context menu
        registerForContextMenu(listView);
        setHasOptionsMenu(true);
        return view;
    }

    public void UpdateList() {
        if (routeAdapter != null)
            routeAdapter.notifyDataSetChanged();
    }

    void loadHeaderAndFooter() {
        //List Header
        View header = View.inflate(getActivity().getApplicationContext(), R.layout.route_row, null);
        ((TextView) header.findViewById(R.id.textViewTitle)).setText(getString(R.string.action_currentlocation));
        ((TextView) header.findViewById(R.id.textViewTime)).setText("");
        ((TextView) header.findViewById(R.id.textViewDistance)).setText("");
        ((ImageView) header.findViewById(R.id.imageView)).setImageResource(R.drawable.route_first_location);
        ((ImageView) header.findViewById(R.id.imageView2)).setImageResource(R.color.transparent);
        listView.addHeaderView(header);

        //List Footer
        View footer = View.inflate(getActivity().getApplicationContext(), R.layout.route_row, null);
        ((TextView) footer.findViewById(R.id.textViewTitle)).setText(getString(R.string.action_addlocation));
        ((TextView) footer.findViewById(R.id.textViewTime)).setText("");
        ((TextView) footer.findViewById(R.id.textViewDistance)).setText("");
        ((ImageView) footer.findViewById(R.id.imageView)).setImageResource(R.drawable.route_add_location);
        ((ImageView) footer.findViewById(R.id.imageView2)).setImageResource(R.color.transparent);
        listView.addFooterView(footer);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_route, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_route_switch) {
            //Load Google Maps Fragment
            ((MainActivity) getActivity()).SwitchFragment(MapsFragment.newInstance(), true, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.routeListView) {
            //Get info
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

            //Get routelist
            if (info.position == 0) {
                menu.setHeaderTitle(getString(R.string.hold_current));
                String[] menuItems = getResources().getStringArray(R.array.hold_current);
                for (int i = 0; i < menuItems.length; i++)
                    menu.add(Menu.NONE, i, i, menuItems[i]);
                return;
            } else if (info.position > 0 && info.position <= routeList.size()) {
                //Clicked on a location

                //Get the temp item
                ActivityItem myItem = routeList.get(info.position - 1);
                menu.setHeaderTitle(UserInfo.getInstance().getLanguage().equals("nl") ? myItem.Title : myItem.TitleEN);

                //Set context menu text
                String[] menuItems = getResources().getStringArray(R.array.hold_location);
                for (int i = 0; i < menuItems.length; i++)
                    menu.add(Menu.NONE, i, i, menuItems[i]);
            } else if (info.position > routeList.size()) {
                //Clicked on the add button
                menu.setHeaderTitle(getString(R.string.hold_add));
                String[] menuItems = getResources().getStringArray(R.array.hold_add);
                for (int i = 0; i < menuItems.length; i++)
                    menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();

        Log.i("Clicked", "Pos:" + info.position + ", index: " + menuItemIndex + ", Routelist size: " + routeList.size());
        if (info.position == 0 && menuItemIndex == 0) {
            //Clicked on current location
            return true;
        } else if (info.position > 0 && info.position <= routeList.size()) {
            //Clicked on a location
            //Get the temp item
            ActivityItem myItem = routeList.get(info.position - 1);
            if (menuItemIndex == 0) { //Delete
                RouteManager.getInstance().Remove(info.position - 1, getActivity().getApplicationContext());
                routeAdapter.notifyDataSetChanged();
            } else if (menuItemIndex == 1) { //View
                ViewActivityFragment fragment = ViewActivityFragment.getInstance();
                fragment.myItem = routeAdapter.getItem(info.position);
                ((MainActivity) getActivity()).SwitchFragment(fragment, false, 0);
            }
            //Set context menu text
            return true;
        } else if (info.position > routeList.size()) {
            //Clicked on the add button
            ((MainActivity) getActivity()).SwitchFragment(CategoryFragment.newInstance().parent = HomeFragment.newInstance(), false, 0);
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        instance = null;
        //mTabPageIndicatorAdapter.removeAll();
    }

    public static class RouteAdapter extends ArrayAdapter<ActivityItem> {

        //Our Imageloader
        ImageLoader imageLoader;

        //Constructor
        RouteAdapter(Context context, int resource, List<ActivityItem> objects) {
            super(context, resource, objects);
            imageLoader = UILManager.instance().getImageLoader(getContext());
        }

        //Reset the list
        public void setItemList(List<ActivityItem> objects) {
            if (objects == null)        //Double Check that objects isn't empty
                return;
            clear();                    //Clear List
            addAll(objects);            //Add items
            notifyDataSetChanged();     //Notify Ourself
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.route_row, parent, false);
            }

            // Get item from the adapter
            final ActivityItem item = getItem(position);

            // Lookup and populate view with Title
            TextView tvTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
            tvTitle.setText(UserInfo.getInstance().getLanguage().equals("nl") ? item.Title : item.TitleEN);

            // Lookup and populate view with Distance
            TextView distance = (TextView) convertView.findViewById(R.id.textViewDistance);
            DecimalFormat df = new DecimalFormat("#.00");
            float tempDist = Float.parseFloat(df.format(item.Distance / 1000).replace(",", "."));
            distance.setText(String.valueOf(tempDist) + " km");

            // Lookup and populate view with Time
            TextView estTime = (TextView) convertView.findViewById(R.id.textViewTime);
            estTime.setText(item.TravelTime);

            return convertView;
        }
    }
}
