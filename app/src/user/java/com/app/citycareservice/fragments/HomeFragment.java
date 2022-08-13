package com.app.citycareservice.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.ui.activities.SearchActivity;
import com.app.citycareservice.adapters.recycler_view.ServicesAdapter;
import com.app.citycareservice.interfaces.api.Authentication;
import com.app.citycareservice.modals.search.service.SearchServiceResponse;
import com.app.citycareservice.utils.ApiClient;
import com.app.citycareservice.utils.FetchLocation;
import com.app.citycareservice.utils.HelperClass;
import com.app.citycareservice.utils.Params;
import com.app.citycareservice.utils.SharedPrefHandler;
import com.google.android.gms.location.LocationRequest;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements Params {

    private static final String TAG = "HomeFragment";

    private Activity activity;

    private TextView search_tv;
    private TextView location_tv;
    private RecyclerView services_rv;

    private ServicesAdapter servicesAdapter;

    private SharedPrefHandler prefHandler;

    private ProgressBar progress_bar;
    private ImageView refresh_iv;

    private FetchLocation fetchLocation;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (Activity) view.getContext();

//        activity.setTheme(R.style.whiteActionBar);
//        HelperClass.makeStatusBarWhite(activity);
        HelperClass.changeStatusBarColor(activity, R.color.white, true);

        search_tv = view.findViewById(R.id.search_tv);
        location_tv = view.findViewById(R.id.location_tv);
        services_rv = view.findViewById(R.id.services_rv);
        progress_bar = view.findViewById(R.id.progress_bar);
        refresh_iv = view.findViewById(R.id.refresh_iv);

        servicesAdapter = new ServicesAdapter(activity);

        prefHandler = new SharedPrefHandler(activity);

        refresh_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLocation();
            }
        });

        fetchLocation = new FetchLocation(activity) {
            @Override
            public void gettingLocation() {

                /*TODO Remove Refreshing after some time if some error occur i.e GPS turned Off*/
                refresh_iv.setVisibility(View.GONE);
                location_tv.setText("Refreshing...");
                progress_bar.setVisibility(View.VISIBLE);
            }

            @Override
            public void addressFetched(Address address) {

                String locality = address.getLocality();
                String subLocality = address.getSubLocality();

                prefHandler.setStringValue(SP_KEY_TEMP_USER_SUB_LOCALITY, subLocality);
                prefHandler.setStringValue(SP_KEY_TEMP_USER_LOCALITY, locality);

                location_tv.setText(subLocality.concat(" , ").concat(locality));
                location_tv.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.GONE);
                refresh_iv.setVisibility(View.VISIBLE);

            }

            @Override
            public void permissionNotGranted() {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE_LOCATION_PERMISSION);
            }

            @Override
            public void gpsIsOff(LocationRequest locationRequest) {
                HelperClass.turnOnGps(activity, locationRequest);
            }

        };

        if (prefHandler.hasKey(SP_KEY_TEMP_USER_LOCALITY) && prefHandler.hasKey(SP_KEY_TEMP_USER_SUB_LOCALITY)) {
            location_tv.setVisibility(View.VISIBLE);
            location_tv.setText(prefHandler.getString(SP_KEY_TEMP_USER_SUB_LOCALITY).concat(" , ").concat(prefHandler.getString(SP_KEY_TEMP_USER_LOCALITY)));
        } else {
            location_tv.setVisibility(View.GONE);
            fetchLocation();
        }
//        searchAPi("");

        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, SearchActivity.class));
            }
        });

    }

    private void fetchLocation() {
        fetchLocation.getLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE_LOCATION_PERMISSION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                fetchLocation();
            else
                HelperClass.showToast(activity, "Permission is required");
        }
    }

//    private void fetchLocation() {
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//            if (isGpsEnabled() && HelperClass.getNetworkInfo(activity))
//                getLocation();
//            else
//                turnOnGps();
//
//        } else {
//            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE_LOCATION_PERMISSION);
//        }
//
//
//    }

//    LocationRequest locationRequest;

//    private void getLocation() {
//
//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(5000);
//        locationRequest.setFastestInterval(2000);
//
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//            return;
//        refresh_iv.setVisibility(View.GONE);
//        location_tv.setText("Refreshing...");
//        progress_bar.setVisibility(View.VISIBLE);
//
//        LocationServices.getFusedLocationProviderClient(activity).requestLocationUpdates(locationRequest, new LocationCallback() {
//            @Override
//            public void onLocationResult(@NonNull LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//
//                LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(this);
//
//                if (locationResult.getLocations().size() > 0) {
//                    int index = locationResult.getLocations().size() - 1;
//
//                    double lat = locationResult.getLocations().get(index).getLatitude();
//                    double lng = locationResult.getLocations().get(index).getLongitude();
//
//                    Address address = getAddress(lat, lng);
//                    Log.d(TAG, "onLocationResult: user location -- " + address);
//
//                    String fullAddress = address.getLocality();
//                    String city = address.getSubLocality();
//
//
//                    prefHandler.setStringValue(SP_KEY_TEMP_USER_SUB_LOCALITY, city);
//                    prefHandler.setStringValue(SP_KEY_TEMP_USER_LOCALITY, fullAddress);
//
////                    prefHandler.setStringValue(SP_KEY_TEMP_USER_CITY, city);
////                    prefHandler.setStringValue(SP_KEY_TEMP_USER_ADDRESS, fullAddress);
//
//                    location_tv.setText(fullAddress);
//                    location_tv.setVisibility(View.VISIBLE);
//                    progress_bar.setVisibility(View.GONE);
//                    refresh_iv.setVisibility(View.VISIBLE);
//                }
//
//            }
//        }, Looper.getMainLooper());
//
//    }
//
//    public Address getAddress(double lat, double lng) {
//        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
//            return addresses.get(0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private void turnOnGps() {
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//        builder.setAlwaysShow(true);
//
//        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(activity)
//                .checkLocationSettings(builder.build());
//
//        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
//            @Override
//            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
//                try {
//                    LocationSettingsResponse response = task.getResult(ApiException.class);
//                    HelperClass.showToast(activity, "GPS is already turned ON");
//                } catch (ApiException e) {
//
//                    switch (e.getStatusCode()) {
//                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                            try {
//                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
//                                resolvableApiException.startResolutionForResult(activity, REQ_CODE_ENABLE_GPS);
//                            } catch (IntentSender.SendIntentException ex) {
//                                ex.printStackTrace();
//                            }
//                            break;
//
//                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                            //Device does not have location
//                            break;
//                    }
//                }
//            }
//        });
//
//    }
//
//
//    private boolean isGpsEnabled() {
//        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//    }

    private void searchAPi(String query) {

        if (HelperClass.getNetworkInfo(activity)) {
//            HelperClass.showLoader(activity);

            SharedPrefHandler prefHandler = new SharedPrefHandler(activity);

            Authentication api = ApiClient.apiService(activity).create(Authentication.class);
            Call<SearchServiceResponse> call = api.serviceSearch(prefHandler.getString(SP_KEY_AUTH_TOKEN), query);
            call.enqueue(new Callback<SearchServiceResponse>() {
                @Override
                public void onResponse(@NonNull Call<SearchServiceResponse> call, @NonNull Response<SearchServiceResponse> response) {

                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        //Token Expired
                        HelperClass.logout(activity, true);
                        return;
                    }
                    SearchServiceResponse apiResponse = response.body();

                    if (response.code() == HttpURLConnection.HTTP_OK && apiResponse != null) {

                        if (apiResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {

//                            servicesAdapter.setData(apiResponse.getResults());

                        } else
                            HelperClass.showToast(activity, apiResponse.getMessage());
                    } else
                        HelperClass.showToast(activity, activity.getString(R.string.something_went_wrong));

//                    HelperClass.hideLoader();
                }

                @Override
                public void onFailure
                        (@NonNull Call<SearchServiceResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: Error is -- " + t);
//                    HelperClass.hideLoader();
                    HelperClass.showToast(activity, activity.getString(R.string.something_went_wrong));
                }
            });

        } else {
//            HelperClass.hideLoader();
            HelperClass.showToast(activity, activity.getString(R.string.check_internet_connection));
        }
    }

}