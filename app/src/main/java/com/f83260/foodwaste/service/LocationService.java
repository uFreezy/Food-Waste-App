package com.f83260.foodwaste.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationService {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 1000;
    public static String TAG = LocationService.class.getName();
    static Context mcontext;
    private static LocationService instance;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location = null;
    myListener listener;
    public LocationListener locationProviderListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            try {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                updateLattitudeLongitude(latitude, longitude);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {


        }

        @Override
        public void onProviderEnabled(String s) {


        }

        @Override
        public void onProviderDisabled(String s) {


        }
    };

    public static synchronized LocationService getInstance(Context ctx) {
        mcontext = ctx;
        if (instance == null) {
            instance = new LocationService();
        }
        return instance;
    }

    public void connectToLocation(myListener listener) {
        this.listener = listener;
        stopLocationUpdates();
        displayLocation();
    }

    private void displayLocation() {
        try {

            Location location = getLocation();
            if (location != null) {
                updateLattitudeLongitude(location.getLatitude(), location.getLongitude());
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mcontext
                    .getSystemService(Context.LOCATION_SERVICE);

            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                this.canGetLocation = false;

            } else {

                this.canGetLocation = true;

                if (isGPSEnabled) {

                    //  Logger.d(TAG + "-->Network", "Network Enabled");

                    if (locationManager != null) {

                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationProviderListener);
                        return location;
                    }

                } else {
                    if (locationManager != null) {

                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationProviderListener);

                        return location;
                    }
                }
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void updateLattitudeLongitude(double latitude, double longitude) {
        //Logger.i(TAG, "updated Lat == " + latitude + "  updated long == " + longitude);
        /*SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance();
        sharedPreferenceManager.updateUserDeviceLatLong(latitude, longitude);*/


        listener.onUpdate(latitude, longitude);
    }

    @SuppressLint("MissingPermission")
    public void stopLocationUpdates() {
        try {
            if (locationManager != null) {
                locationManager.removeUpdates(locationProviderListener);
                locationManager = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface myListener {
        void onUpdate(double latt, double longg);
    }
}
