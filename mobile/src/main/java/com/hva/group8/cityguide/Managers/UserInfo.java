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
import com.hva.group8.cityguide.MainActivity;
import com.hva.group8.cityguide.MapsFragment;
import com.hva.group8.cityguide.MyApp;
import com.hva.group8.cityguide.RouteFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Lansenou on 8-5-2015.
 */
public class UserInfo implements LocationListener {
    //Singleton
    private static UserInfo instance;
    public boolean sendNotifications = true;
    public Toast toast;
    public LocationManager manager;

    private Context context;
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

            //Get all the providers
            List<String> providers = manager.getProviders(true);

            //Loop through all the providers
            for (String provider : providers) {
                //Setup a location update requester
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 3, this);
            }
            //Add GPS Listener0
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
        toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public float getDistance(Context context, double targetLong, double targetLat) {
        //Context can't be null
        if (context == null) {
            context = MainActivity.getInstance().getApplicationContext();
            if (context == null) {
                Log.e("userInfo", "Context was null");
                return 0;
            }
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

        //String sHours = String.valueOf(hours);
        //String sMinutes = minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes)  + "";
        return String.valueOf(hours) + "h "  + String.valueOf(minutes) + "min";
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
            //newToast(context, context.getText(R.string.gps_not_found).toString(), Toast.LENGTH_LONG);
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

        if (context == null) {
            context = MyApp.getContext();
            if (context == null) {
                context = MainActivity.getInstance().getApplicationContext();
            }
        }

        if (manager == null && context  != null)
            manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (manager == null)
            return new Location("empty");

        //Get all the providers
        try {
            List<String> providers = manager.getProviders(true);

            if (providers.isEmpty() || providers == null) {
                providers = new ArrayList<>();
                providers.add("NETWORK_PROVIDER");
                providers.add("GPS_PROVIDER");
            }


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
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        return bestLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocationList();
        MapsFragment.getInstance().onLocationChanged(location);
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
