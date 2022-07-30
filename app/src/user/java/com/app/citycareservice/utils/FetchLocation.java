package com.app.citycareservice.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.app.citycareservice.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public abstract class FetchLocation implements Params {

    private static final String TAG = "FetchLocation";

    private final Context activity;
    private final SharedPrefHandler prefHandler;
    private final LocationRequest locationRequest;

    public abstract void gettingLocation();

    public abstract void addressFetched(Address address);

    public abstract void permissionNotGranted();

    public abstract void gpsIsOff(LocationRequest locationRequest);

    public FetchLocation(Activity activity) {
        this.activity = activity;
        prefHandler = new SharedPrefHandler(activity);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
    }

//    private void fetchLocation() {
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//            if (HelperClass.getNetworkInfo(activity)) {
//                if (!HelperClass.isGpsEnabled((Activity) activity))
//                    HelperClass.turnOnGps((Activity) activity, locationRequest);
////                gpsIsOff();
//                else
//                    getLocation();
//            } else
//                HelperClass.showToast(activity, "No Internet Connection");
//
//        } else {
//            permissionNotGranted();
//        }
//
//    }

    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionNotGranted();
            return;
        }

        if (!HelperClass.isGpsEnabled(activity)) {
            gpsIsOff(locationRequest);
            return;
        }

        if (!HelperClass.getNetworkInfo(activity)) {
            HelperClass.showToast(activity, activity.getString(R.string.check_internet_connection));
            return;
        }

        gettingLocation();

        LocationServices.getFusedLocationProviderClient(activity).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(this);

                if (locationResult.getLocations().size() > 0) {
                    int index = locationResult.getLocations().size() - 1;

                    double lat = locationResult.getLocations().get(index).getLatitude();
                    double lng = locationResult.getLocations().get(index).getLongitude();

                    Address address = getAddress(lat, lng);
                    Log.i(TAG, "onLocationResult: user location -- " + address);

                    addressFetched(address);

                }

            }
        }, Looper.getMainLooper());

    }

    public Address getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
