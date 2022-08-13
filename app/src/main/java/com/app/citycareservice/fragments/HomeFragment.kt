package com.app.citycareservice.fragments

import android.Manifest
import android.app.Activity
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.citycareservice.adapters.recycler_view.ServicesAdapter
import android.widget.ProgressBar
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.app.citycareservice.R
import android.content.Intent
import com.app.citycareservice.activities.SearchActivity
import android.content.pm.PackageManager
import android.location.Address
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.app.citycareservice.adapters.recycler_view.BookingHistoryAdapter
import com.app.citycareservice.databinding.FragmentHistoryBinding
import com.app.citycareservice.databinding.FragmentHomeBinding
import com.app.citycareservice.interfaces.api.Authentication
import com.app.citycareservice.modals.search.service.SearchServiceResponse
import com.app.citycareservice.fragments.HomeFragment
import com.app.citycareservice.interfaces.order.Service
import com.app.citycareservice.modals.allService.AllServicesResposne
import com.app.citycareservice.modals.order.myOrders.MyOrdersResponse
import com.app.citycareservice.utils.*
import com.google.android.gms.location.LocationRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class HomeFragment : Fragment(), Params {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var activity: Activity
    private lateinit var fetchLocation: FetchLocation
    private lateinit var prefHandler: SharedPrefHandler
    private lateinit var servicesAdapter: ServicesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        activity = binding.root.context as Activity
        return binding.root
//        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HelperClass.changeStatusBarColor(activity, R.color.white, true)

//        servicesAdapter = ServicesAdapter(activity)
        prefHandler = SharedPrefHandler(activity)

        with(binding) {
            refreshIv.setOnClickListener(View.OnClickListener { fetchLocation() })
            fetchLocation = object : FetchLocation(activity) {
                override fun gettingLocation() {

                    /*TODO Remove Refreshing after some time if some error occur i.e GPS turned Off*/
                    refreshIv.visibility = View.GONE
                    locationTv.text = "Refreshing..."
                    progressBar.visibility = View.VISIBLE
                }

                override fun addressFetched(address: Address) {
                    val locality = address.locality
                    val subLocality = address.subLocality
                    prefHandler.setStringValue(SP_KEY_TEMP_USER_SUB_LOCALITY, subLocality)
                    prefHandler.setStringValue(SP_KEY_TEMP_USER_LOCALITY, locality)
                    locationTv.text = "$subLocality , $locality"
                    locationTv.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    refreshIv.visibility = View.VISIBLE
                }

                override fun permissionNotGranted() {
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQ_CODE_LOCATION_PERMISSION
                    )
                }

                override fun gpsIsOff(locationRequest: LocationRequest) {
                    HelperClass.turnOnGps(activity, locationRequest)
                }
            }
            if (prefHandler.hasKey(Params.SP_KEY_TEMP_USER_LOCALITY) && prefHandler.hasKey(
                    Params.SP_KEY_TEMP_USER_SUB_LOCALITY
                )
            ) {
                locationTv.visibility = View.VISIBLE
                locationTv.text =
                    prefHandler.getString(Params.SP_KEY_TEMP_USER_SUB_LOCALITY) + " , " + prefHandler.getString(
                        Params.SP_KEY_TEMP_USER_LOCALITY

                    )
            } else {
                locationTv.visibility = View.GONE
                fetchLocation()
            }
            //        searchAPi("");
            searchTv.setOnClickListener {
                startActivity(
                    Intent(
                        activity,
                        SearchActivity::class.java
                    )
                )
            }
        }

        getHomeData()
    }

    private fun fetchLocation() {
        fetchLocation.getLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Params.REQ_CODE_LOCATION_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) fetchLocation() else HelperClass.showToast(
                activity,
                "Permission is required"
            )
        }
    }


    private fun getHomeData() {
        if (HelperClass.getNetworkInfo(activity)) {
            HelperClass.showLoader(activity)
            val prefHandler = SharedPrefHandler(activity)
            val api = ApiClient.apiService(activity).create(Service::class.java)
            val call =
                api.getAllServices(prefHandler.getString(Params.SP_KEY_AUTH_TOKEN) /*page_no*/)
            call.enqueue(object : Callback<AllServicesResposne> {
                override fun onResponse(
                    call: Call<AllServicesResposne>,
                    response: Response<AllServicesResposne>
                ) {
                    HelperClass.hideLoader()

                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        //Token Expired
                        HelperClass.logout(activity, true)
                        return
                    }
                    val apiResponse = response.body()
                    if (response.code() == HttpURLConnection.HTTP_OK && apiResponse != null) {

//                        has_more_data = apiResponse.getTotal_pages() > page_no;
                        if (apiResponse.status) {

                            servicesAdapter = ServicesAdapter(activity, apiResponse.results)
                            binding.servicesRv.adapter = servicesAdapter


                            /* if (pageNo == 1) historyAdapter.setData(apiResponse.results)
                             else historyAdapter.addData(apiResponse.results)*/
                        } else HelperClass.showToast(activity, apiResponse.message)
                    } else HelperClass.showToast(
                        activity,
                        activity.getString(R.string.something_went_wrong)
                    )
                }

                override fun onFailure(call: Call<AllServicesResposne>, t: Throwable) {
                    Log.d(TAG, "onFailure: Error is -- $t")
                    HelperClass.hideLoader()
                    HelperClass.showToast(
                        activity,
                        activity!!.getString(R.string.something_went_wrong)
                    )
                }
            })
        } else HelperClass.showToast(
            activity,
            activity.getString(R.string.check_internet_connection)
        )
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
    private fun searchAPi(query: String) {
        if (HelperClass.getNetworkInfo(activity)) {
//            HelperClass.showLoader(activity);
            val prefHandler = SharedPrefHandler(activity)
            val api = ApiClient.apiService(activity).create(
                Authentication::class.java
            )
            val call = api.serviceSearch(prefHandler.getString(Params.SP_KEY_AUTH_TOKEN), query)
            call.enqueue(object : Callback<SearchServiceResponse?> {
                override fun onResponse(
                    call: Call<SearchServiceResponse?>,
                    response: Response<SearchServiceResponse?>
                ) {
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        //Token Expired
                        HelperClass.logout(activity, true)
                        return
                    }
                    val apiResponse = response.body()
                    if (response.code() == HttpURLConnection.HTTP_OK && apiResponse != null) {
                        if (apiResponse.statusCode == HttpURLConnection.HTTP_OK) {

//                            servicesAdapter.setData(apiResponse.getResults());
                        } else HelperClass.showToast(activity, apiResponse.message)
                    } else HelperClass.showToast(
                        activity,
                        activity!!.getString(R.string.something_went_wrong)
                    )

//                    HelperClass.hideLoader();
                }

                override fun onFailure(call: Call<SearchServiceResponse?>, t: Throwable) {
                    Log.d(TAG, "onFailure: Error is -- $t")
                    //                    HelperClass.hideLoader();
                    HelperClass.showToast(
                        activity,
                        activity!!.getString(R.string.something_went_wrong)
                    )
                }
            })
        } else {
//            HelperClass.hideLoader();
            HelperClass.showToast(
                activity,
                activity.getString(R.string.check_internet_connection)
            )
        }
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}