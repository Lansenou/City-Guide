package com.hva.group8.cityguide.GoogleMaps;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DirectionItem {

    private ArrayList<LatLng> geoPoints;
    private ArrayList<LatLng> startPoints;
    private ArrayList<String> HTMLDirection;
    private ArrayList<String> images;

    public DirectionItem(ArrayList<LatLng> geoPoints, ArrayList<String> HTMLDirection, ArrayList<LatLng> startPoints, ArrayList<String> images) {
        this.geoPoints = geoPoints;
        this.HTMLDirection = HTMLDirection;
        this.startPoints = startPoints;
        this.images = images;
    }

    public ArrayList<LatLng> getGeoPoints() {
        return geoPoints;
    }

    public ArrayList<LatLng> getStartPoints() {
        return startPoints;
    }

    public ArrayList<String> getHTMLDirection() {
        return HTMLDirection;
    }

    public ArrayList<String> getImages() {
        return images;
    }


}

