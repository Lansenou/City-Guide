package com.hva.group8.cityguide.Managers;

import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.hva.group8.cityguide.ActivityItem;
import com.hva.group8.cityguide.R;
import com.hva.group8.cityguide.RouteFragment;

import java.util.List;
import java.util.Locale;

/**
 * Created by Lansenou on 8-5-2015.
 */
public class UserInfo implements LocationListener {
    //Singleton
    private static UserInfo instance;
    public boolean sendNotifications = true;
    Context context;
    private LocationManager manager;
    private Toast toast;
    private String language = "";
    private String travelMode = "walking"; // walking \ driving \ bicycling \ transit

    public static UserInfo getInstance() {
        if (instance == null) {
            instance = new UserInfo();
        }
        return instance;
    }

    public void instantiate(Context context) {
        if (manager == null) {
            getInstance();
            //Set our location manager
            manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            this.context = context;

            //Setup a location update requester
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000 * 60 * 1, // 1 minute
                    10,            // 10 meter
                    this);

            //Add GPS Listener
            manager.addGpsStatusListener(new GpsStatus.Listener() {
                @Override
                public void onGpsStatusChanged(int event) {
                    updateLocationList();
                }
            });
        }
    }

    public void newToast(Context context, String text, int duration) {
        if (!sendNotifications)
            return;
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(this.context, text, duration);
        toast.show();
    }

    public float getDistance(Context context, double targetLong, double targetLat) {
        //Context can't be null
        if (context == null) {
            Log.e("userInfo", "Context was null");
            return 0;
        }
        if (manager == null) {
            instantiate(context);
        }
        return calculateDistance(targetLong, targetLat);
    }

    public String getTime(float dist) {
        float time = (dist / 4500);

        int hours = (int) time;
        int minutes = (int) ((time - hours) * 60);

        String sHours = hours < 10 ? "0" + String.valueOf(hours) : String.valueOf(hours) + "";
        String sMinutes = minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes) + "";
        return sHours + "h:" + sMinutes + "m";
    }

    public String getTravelMode() {
        return travelMode;
    }

    public String getLanguage() {
        if (language.isEmpty())
            setLanguage(Locale.getDefault().getLanguage());
        return language;
    }

    public void setLanguage(String lang) {
        language = lang;
        Log.i("Language:", language);
    }

    public LatLng getLatLng() {
        //Get the last known location from the gps
        Location locationA = getLastKnownLocation();
        //Location Can't be null
        if (locationA == null) {
            newToast(context, context.getText(R.string.gps_not_found).toString(), Toast.LENGTH_LONG);
            locationA = new Location("Amsterdam");
            locationA.setLatitude(52.370215700000000000);    //DEFAULT AMSTERDAM LAT
            locationA.setLongitude(4.895167899999933000);    //DEFAULT AMSTERDAM LONG
        }
        return new LatLng(locationA.getLatitude(), locationA.getLongitude());
    }


    private float calculateDistance(double targetLong, double targetLat) {
        //Manager can't be null
        if (manager == null) {
            Log.e("userInfo", "Manager was null");
            return 0;
        }
        //Get the last known location from the gps
        Location locationA = getLastKnownLocation();

        //Location Can't be null
        if (locationA == null) {
            newToast(context, context.getText(R.string.gps_not_found).toString(), Toast.LENGTH_SHORT);
            locationA = new Location("Amsterdam");
            locationA.setLatitude(52.370215700000000000);    //DEFAULT AMSTERDAM LAT
            locationA.setLongitude(4.895167899999933000);    //DEFAULT AMSTERDAM LONG
        }

        //Set location B to compare it with A
        Location locationB = new Location("target");
        locationB.setLongitude(targetLong);
        locationB.setLatitude(targetLat);
        return locationA.distanceTo(locationB);
    }

    private Location getLastKnownLocation() {
        Location bestLocation = null;

        //Get all the providers
        List<String> providers = manager.getProviders(true);

        //Loop through all the providers
        for (String provider : providers) {
            Location l = manager.getLastKnownLocation(provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocationList();
    }

    public void updateLocationList() {
        List<ActivityItem> routeList = RouteManager.getInstance().routeList;
        for (int i = 0; i < routeList.size(); i++) { //Loop through our activity list and update them
            ActivityItem item = routeList.get(i);
            item.Distance = calculateDistance(item.Longitude, item.Latitude);

            //Update List
            RouteFragment.getInstance().UpdateList();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
