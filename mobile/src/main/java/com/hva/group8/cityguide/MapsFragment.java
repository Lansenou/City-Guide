/*
 * Copyright (c) 2015.
 * Created by Lansenou on 23/05/15 01:59
 *
 */

package com.hva.group8.cityguide;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hva.group8.cityguide.GoogleMaps.GoogleDirection;
import com.hva.group8.cityguide.Managers.RouteManager;
import com.hva.group8.cityguide.Managers.UserInfo;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lansenou on 22-5-2015.
 */

public class MapsFragment extends Fragment {

    private static MapsFragment instance;
    List<ActivityItem> list = RouteManager.getInstance().routeList;
    Location location;
    TextView textProgress;
    private UserInfo userInfo = UserInfo.getInstance();
    LatLng ourLocation = userInfo.getLatLng();
    private List<ActivityItem> routeList = RouteManager.getInstance().routeList;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private SupportMapFragment fragment;
    private GoogleDirection googleDirection;
    private Document mDoc; // Maps doc

    public static MapsFragment getInstance() {
        if (instance == null)
            instance = new MapsFragment();
        return instance;
    }

    public static MapsFragment newInstance() {
        return (instance = new MapsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        textProgress = (TextView) view.findViewById(R.id.textView);

        setHasOptionsMenu(true);

        setUpMapIfNeeded();


        return view;
    }

    /**
     * ** Sets up the map if it is possible to do so ****
     */
    public void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = fragment.getMap();
            if (mMap != null)
                setUpMap();
                // Check if we were successful in obtaining the map.
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        Log.i(",", location.getLatitude() + ", " + location.getLongitude());
                        setUpMap();
                    }
                });
        }
    }

    public void setUpMap() {
        if (mMap == null)
            return;

        //Clear map before drawing
        mMap.clear();

        //Add our location to the map
        mMap.addMarker(new MarkerOptions().position(ourLocation).title("You are here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).snippet(userInfo.getLatLng().toString()));

        setupDirections();

        //Move camera to our location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ourLocation, 15.0f));

        requestDirection();
    }

    void requestDirection() {
        if (list.size() <= 0)
            return;

        ArrayList<LatLng> itemList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            LatLng lastLocation;
            if (i == 0)
                lastLocation = userInfo.getLatLng();
            else {
                ActivityItem lastItem = list.get(i - 1);
                lastLocation = new LatLng(lastItem.Latitude, lastItem.Longitude);
            }
            ActivityItem item = list.get(i);

            googleDirection.request(lastLocation, new LatLng(item.Latitude, item.Longitude), GoogleDirection.MODE_WALKING);

            //Markers
            //Create a new marker to show on the map
            MarkerOptions marker = new MarkerOptions();
            //Position
            marker.position(new LatLng(item.Latitude, item.Longitude));
            //Title
            marker.title(userInfo.getLanguage().equals("nl") ? item.Title : item.TitleEN);
            //Description
            String snippet = (userInfo.getLanguage().equals("nl") ? item.Calender : item.CalenderEN);
            marker.snippet(snippet);
            //Add marker to the map
            mMap.addMarker(marker);
        }

        googleDirection.setLogging(true);

    }

    void setupDirections() {
        if (googleDirection != null)
            return;

        //Instantiate GoogleDirection
        googleDirection = new GoogleDirection(getActivity().getApplicationContext());
        googleDirection.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {
            public void onResponse(String status, Document doc, GoogleDirection gd) {
                mDoc = doc;
                //Add a line to our map
                mMap.addPolyline(googleDirection.getPolyline(doc, 3, Color.RED));
            }
        });
    }


    void addMarkers(PolylineOptions line) {
        //Loop through the location list, add these to the map and the line
        for (int i = 0; i < routeList.size(); i++) {
            ActivityItem item = routeList.get(i);
            //Create a new marker to show on the map
            MarkerOptions marker = new MarkerOptions();
            //Position
            marker.position(new LatLng(item.Latitude, item.Longitude));
            //Title
            marker.title(userInfo.getLanguage().equals("nl") ? item.Title : item.TitleEN);
            //Description
            String snippet = (userInfo.getLanguage().equals("nl") ? item.Calender : item.CalenderEN);
            marker.snippet(snippet);
            //Add marker to the map
            mMap.addMarker(marker);
            //Add location to the line
            line.add(new LatLng(item.Latitude, item.Longitude));
        }
        //Add line to the map
        mMap.addPolyline(line);
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
            ((MainActivity) getActivity()).SwitchFragment(RouteFragment.newInstance(), true, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMap = null;
        instance = null;
        //mTabPageIndicatorAdapter.removeAll();
    }
}
