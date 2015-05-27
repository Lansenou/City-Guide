package com.hva.group8.cityguide.GoogleMaps;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Reinier on 27-5-2015.
 */
public class DirectionItem {

    private ArrayList<LatLng> geoPoints;
    private ArrayList<String> HTMLDirection;

    public DirectionItem(ArrayList<LatLng> geoPoints, ArrayList<String> HTMLDirection) {
        this.geoPoints = geoPoints;
        this.HTMLDirection = HTMLDirection;
    }

    public ArrayList<LatLng> getGeoPoints() {
        return geoPoints;
    }

    public ArrayList<String> getHTMLDirection() {
        return HTMLDirection;
    }

}
