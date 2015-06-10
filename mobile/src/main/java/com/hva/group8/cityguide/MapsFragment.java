/*
 * Copyright (c) 2015.
 * Created by Lansenou on 23/05/15 01:59
 *
 */

package com.hva.group8.cityguide;

import android.content.ClipData;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import com.hva.group8.cityguide.Managers.UILManager;
import com.hva.group8.cityguide.Managers.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

    private Document currentDoc; // Maps doc
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

        setHasOptionsMenu(true);

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
                SetupDirection();
                setUpMap();

                //Move camera to our location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ourLocation, 12.0f));

                mMap.setMyLocationEnabled(true);

                requestDirection();

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

    void SetupDirection() {
        //Instantiate GoogleDirection
        googleDirection = new GoogleDirection(getActivity().getApplicationContext());
        googleDirection.setOnDirectionResponseListener(new GoogleDirection.OnDirectionResponseListener() {

            @Override
            public void onResponse(String status, Document doc, GoogleDirection gd, int id) {
                //Called when our item was the first item

                Log.i("Item Number", id + ".");
                if (id == 0) { //Our next location
                    currentDoc = doc;
                    mItem = gd.getDirection(doc);
                    Log.e("Item", mItem.getHTMLDirection().toString());
                }

                ActivityItem item = list.get(id);

                Log.i("Item", id + item.Title + item.Longitude + item.Latitude);

                //Markers
                //Create a new marker to show on the map
                MarkerOptions marker = new MarkerOptions();
                //Position
                marker.position(new LatLng(item.Latitude, item.Longitude));
                //Title
                marker.title(userInfo.getLanguage().equals("nl") ? item.Title : item.TitleEN);

                try {
                    //Description
                    String snippet = (userInfo.getLanguage().equals("nl") ? item.Calender : item.CalenderEN);
                    if (snippet != "0" || snippet.isEmpty())
                        marker.snippet(snippet);
                } catch (Exception e) {
                    Log.e("Marker", e.toString());
                }
                //Add marker to the map
                if (mMap != null)
                    mMap.addMarker(marker);

                onResponse(status, doc, gd);
            }

            @Override
            public void onResponse(String status, Document doc, GoogleDirection gd) {
                //Called once or never
                if (currentDoc == null) {
                    currentDoc = doc;
                    mItem = gd.getDirection(doc);
                    Log.e("Item", mItem.getHTMLDirection().toString());
                }

                //Add a line to our map
                if (mMap != null) {
                    mMap.addPolyline(googleDirection.getPolyline(doc, 3, Color.RED));
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Location mLocation = new Location("");
        requestDirection();

        Log.i("My Location", userInfo.getLatLng().toString());

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

            //HAY
            float meh = (directions.get(minIndex));
            int metersTillDest = (int) meh;

            String wantedText = mItem.getHTMLDirection().get(minIndex) + " in " + metersTillDest + "m";

            Log.i("Test", wantedText );

            if (metersTillDest <= 45) {
                textProgress.setText(Html.fromHtml(wantedText));
            } else textProgress.setText("Follow the path");

            //Send our message to the smartwatch
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {

                Bitmap background = list.get(minIndex).ImageBitmap;

                Bitmap maneuver = getBitmapFromString(mItem.getImages().get(minIndex));

                activity.sendMessage("/direction:" + wantedText, background, maneuver);
            }
        }
    }

    Bitmap getBitmapFromString(String maneuver) {
        if (maneuver == null || maneuver.isEmpty()) {
            Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
            return Bitmap.createBitmap(10, 10, conf); // this creates a MUTABLE bitmap
        }
        Resources res = getResources();
        String mDrawableName = maneuver.replace("-", "_");
        int resID = res.getIdentifier(mDrawableName, "drawable", getActivity().getApplicationContext().getPackageName());
        return BitmapFactory.decodeResource(res, resID);
    }



    public void setUpMap() {
        if (mMap == null)
            return;

        //Clear map before drawing
        mMap.clear();

        if (currentDoc != null)
            mMap.addPolyline(googleDirection.getPolyline(currentDoc, 3, Color.RED));
    }



    void requestDirection() {
        // Hey
        if (list.size() <= 0)
            return;

        mMap.clear();

        for (int i = 0; i < list.size(); i++) {
            LatLng lastLocation;
            if (i == 0)
                lastLocation = userInfo.getLatLng();
            else {
                ActivityItem lastItem = list.get(i - 1);
                lastLocation = new LatLng(lastItem.Latitude, lastItem.Longitude);
            }
            final ActivityItem item = list.get(i);
            googleDirection.request(lastLocation, new LatLng(item.Latitude, item.Longitude), GoogleDirection.MODE_WALKING, i);
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
