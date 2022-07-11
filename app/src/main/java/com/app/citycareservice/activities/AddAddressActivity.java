package com.app.citycareservice.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.citycareservice.R;
import com.app.citycareservice.modals.AddressModal.AddressModal;
import com.app.citycareservice.utils.FetchLocation;
import com.app.citycareservice.utils.HelperClass;
import com.app.citycareservice.utils.Params;
import com.app.citycareservice.utils.SharedPrefHandler;
import com.app.citycareservice.utils.roomDB.AddressDatabase;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class AddAddressActivity extends AppCompatActivity implements Params {

    private static final String TAG = "AddAddressActivity";
    private Activity activity;

    private String id = DEFAULT_EMPTY_STRING;

    private TextView place_name_tv;
    private TextView address_tv;
    private ImageView refresh_iv;
    private ProgressBar progress_bar;
    private TextInputLayout house_no_tif;
    private TextInputLayout landmark_tif;
    private TextInputLayout name_tif;
    private MaterialButton save_btn;

    private SharedPrefHandler prefHandler;

    private FetchLocation fetchLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_sheet_add_address);

        place_name_tv = findViewById(R.id.place_name_tv);
        activity = AddAddressActivity.this;

        address_tv = findViewById(R.id.address_tv);
        house_no_tif = findViewById(R.id.house_no_tif);
        landmark_tif = findViewById(R.id.landmark_tif);
        name_tif = findViewById(R.id.name_tif);
        save_btn = findViewById(R.id.save_btn);
        refresh_iv = findViewById(R.id.refresh_iv);
        progress_bar = findViewById(R.id.progress_bar);

        prefHandler = new SharedPrefHandler(activity);

        if (prefHandler.hasKey(SP_KEY_TEMP_USER_LOCALITY)) {
            String address = prefHandler.getString(SP_KEY_TEMP_USER_LOCALITY);
            String city = prefHandler.getString(SP_KEY_TEMP_USER_SUB_LOCALITY);

            place_name_tv.setText(city);
            address_tv.setText(address);
        } else {
//            location_tv.setVisibility(GONE);
            fetchLocation();
        }

        refresh_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLocation();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Saved");
                validateForm();
            }
        });

    }

    private void validateForm() {
        Log.d(TAG, "validateForm: validaetForm");

        house_no_tif.setErrorEnabled(false);
        landmark_tif.setErrorEnabled(false);
        name_tif.setErrorEnabled(false);

        String place = place_name_tv.getText().toString().trim();
        String address = address_tv.getText().toString().trim();
        String house_no = Objects.requireNonNull(house_no_tif.getEditText()).getText().toString().trim();
        String landmark = Objects.requireNonNull(landmark_tif.getEditText()).getText().toString().trim();
        String name = Objects.requireNonNull(name_tif.getEditText()).getText().toString().trim();

        if (TextUtils.isEmpty(place) || TextUtils.isEmpty(address)) {
            HelperClass.showToast(activity, "Please Update Location");
            return;
        }
        if (TextUtils.isEmpty(house_no)) {
            house_no_tif.setError("House Number cannot be empty");
            house_no_tif.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(name)) {
            name_tif.setError("Name cannot be empty");
            name_tif.setFocusable(true);
            return;
        }

        addAddressApi(house_no, place, address, landmark, name);

    }

    private void addAddressApi(String house_no, String place, String address, String landmark, String name) {
        //Hit Address API

        Log.d(TAG, "addAddressApi: ");
        AddressModal addressModal = new AddressModal();
        addressModal.setHouseNo(house_no);
        addressModal.setName(name);
        addressModal.setLandmark(landmark);
        addressModal.setPlace(place);
        addressModal.setAddress(address);

        AddressDatabase addressDatabase = AddressDatabase.getInstance(activity);
 /*       if (!TextUtils.isEmpty(id) || !TextUtils.equals(DEFAULT_EMPTY_STRING, id) || addressDatabase.addressDAO().addressExists("id", id))
            addressDatabase.addressDAO().updateAddress(addressModal);
        else*/
        addressDatabase.addressDAO().insertAddress(addressModal);

        HelperClass.showToast(activity, "Address Added Successfully");
        finish();
    }

    private void fetchLocation() {
        Log.d(TAG, "fetchLocation: ");
        fetchLocation = new FetchLocation(activity) {
            @Override
            public void gettingLocation() {
                Log.d(TAG, "gettingLocation: ");

                refresh_iv.setVisibility(GONE);
                place_name_tv.setText("Refreshing...");
                address_tv.setText("Refreshing...");
                progress_bar.setVisibility(VISIBLE);

            }

            @Override
            public void addressFetched(Address address) {
                Log.d(TAG, "addressFetched: ");

                String fullAddress = address.getLocality();
                String city = address.getSubLocality();

                prefHandler.setStringValue(SP_KEY_TEMP_USER_SUB_LOCALITY, city);
                prefHandler.setStringValue(SP_KEY_TEMP_USER_LOCALITY, fullAddress);

                place_name_tv.setText(city);
                address_tv.setText(fullAddress);
                place_name_tv.setVisibility(VISIBLE);
                address_tv.setVisibility(VISIBLE);
                progress_bar.setVisibility(GONE);
                refresh_iv.setVisibility(VISIBLE);

            }

            @Override
            public void permissionNotGranted() {
                Log.d(TAG, "permissionNotGranted: ");
                ActivityCompat.requestPermissions((Activity) activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            }

            @Override
            public void gpsIsOff(LocationRequest locationRequest) {
                HelperClass.turnOnGps(activity,locationRequest);
            }
//
//            @Override
//            public void gpsIsOff() {
//                HelperClass.turnOnGps(activity, locationRequest);
//            }
        };

        fetchLocation.getLocation();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_ENABLE_GPS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    fetchLocation.getLocation();
                case Activity.RESULT_CANCELED:
                    HelperClass.showToast(activity, "Please Turn On Gps");
            }
        }
    }


    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == REQ_CODE_ENABLE_GPS) {
//
//            switch (resultCode) {
//                case Activity.RESULT_OK:
//                    fetchLocation.getLocation();
//                case Activity.RESULT_CANCELED:
//                    HelperClass.showToast(activity, "Please Turn On Gps");
//            }
//        }
//    }


    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        if (requestCode == REQ_CODE_ENABLE_GPS) {
//
//            switch (resultCode) {
//                case Activity.RESULT_OK:
//                    fetchLocation.getLocation();
//                case Activity.RESULT_CANCELED:
//                    HelperClass.showToast(activity, "Please Turn On Gps");
//            }
//        }
//
//    }

}
