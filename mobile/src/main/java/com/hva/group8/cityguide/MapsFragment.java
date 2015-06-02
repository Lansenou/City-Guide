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
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hva.group8.cityguide.GoogleMaps.DirectionItem;
import com.hva.group8.cityguide.GoogleMaps.GoogleDirection;
import com.hva.group8.cityguide.Managers.RouteManager;
import com.hva.group8.cityguide.Managers.UserInfo;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Lansenou on 22-5-2015.
 */

public class MapsFragment extends Fragment implements LocationSource.OnLocationChangedListener {

    //Singleton
    private static MapsFragment instance;
    private List<ActivityItem> list = RouteManager.getInstance().routeList;
    private TextView textProgress;
    private UserInfo userInfo = UserInfo.getInstance();
    private LatLng ourLocation = userInfo.getLatLng();
    private SupportMapFragment fragment;
    private GoogleDirection googleDirection;
    private Document mDoc; // Maps doc
    private DirectionItem mItem;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

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
        textProgress = (TextView) view.findViewById(R.id.textView2);
        textProgress.setText("test");

        setHasOptionsMenu(true);

        //Instantiate GoogleDirection
        googleDirection = new GoogleDirection(getActivity().getApplicationContext());

        setUpMapIfNeeded();

        if (instance == null || instance != this)
            instance = this;

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
            if (mMap != null) {
                setUpMap();

                mMap.setMyLocationEnabled(true);

                // Check if we were successful in obtaining the map.
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        Log.i(",", location.getLatitude() + ", " + location.getLongitude());
                        onLocationChanged(location);
                    }
                });
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        String direction = "";
        Location mLocation = new Location("");

        if (mItem != null) {
            ArrayList<Float> directions = new ArrayList<>();
            ArrayList<LatLng> startPoints = mItem.getStartPoints();
            for (int i = 0; i < startPoints.size(); i++) {
                mLocation.setLatitude(startPoints.get(i).latitude);
                mLocation.setLongitude(startPoints.get(i).longitude);
                Log.wtf("dist", i + "distance to " + location.distanceTo(mLocation));
                directions.add(location.distanceTo(mLocation));
            }
            //New test
            int minIndex = directions.indexOf(Collections.min(directions));
            Log.i("Size", mItem.getHTMLDirection().size() + ", tried to get" + minIndex);

            String wantedText = mItem.getHTMLDirection().get(minIndex);
            Log.i("Test", wantedText);
            if (directions.get(minIndex) <= 45) {
                textProgress.setText(wantedText);
            } else textProgress.setText("Follow the path");

        }
    }

    public void setUpMap() {
        if (mMap == null)
            return;

        //Clear map before drawing
        mMap.clear();

        //Add our location to the map
        //mMap.addMarker(new MarkerOptions().position(ourLocation).title("You are here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).snippet(userInfo.getLatLng().toString()));

        if (mDoc != null)
            mMap.addPolyline(googleDirection.getPolyline(mDoc, 3, Color.RED));

        //Move camera to our location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ourLocation, 15.0f));

        requestDirection();
    }

    void requestDirection() {
        // Hey
        if (list.size() <= 0)
            return;

        // Item
        if (mItem != null)
            return;


        for (int i = 0; i < 1; i++) {
            LatLng lastLocation;
            if (i == 0)
                lastLocation = userInfo.getLatLng();
            else {
                ActivityItem lastItem = list.get(i - 1);
                lastLocation = new LatLng(lastItem.Latitude, lastItem.Longitude);
            }
            final ActivityItem item = list.get(i);

            googleDirection.request(lastLocation, new LatLng(item.Latitude, item.Longitude), GoogleDirection.MODE_WALKING);
            googleDirection.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {
                @Override
                public void onResponse(String status, Document doc, GoogleDirection gd) {
                    mDoc = doc;
                    mItem = gd.getDirection(mDoc);
                    Log.e("Item", mItem.getHTMLDirection().toString());

                    //Our location changed
                    Location myLocation = new Location("myLocation");
                    myLocation.setLongitude(ourLocation.longitude);
                    myLocation.setLatitude(ourLocation.latitude);
                    onLocationChanged(myLocation);

                    //Add a line to our map
                    mMap.addPolyline(googleDirection.getPolyline(doc, 3, Color.RED));

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
            });
        }
        googleDirection.setLogging(true);
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
