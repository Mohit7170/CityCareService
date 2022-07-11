package com.app.citycareservice.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.adapters.recycler_view.SelectDateAdapter;
import com.app.citycareservice.adapters.recycler_view.SelectTimeAdapter;
import com.app.citycareservice.dialogs.bottomSheet.SelectAddressBottomSheet;
import com.app.citycareservice.interfaces.click.AddressSelect;
import com.app.citycareservice.interfaces.click.DateSelect;
import com.app.citycareservice.interfaces.click.TimeSelect;
import com.app.citycareservice.interfaces.order.Service;
import com.app.citycareservice.modals.AddressModal.AddressModal;
import com.app.citycareservice.modals.search.service.SearchServiceResponse;
import com.app.citycareservice.utils.ApiClient;
import com.app.citycareservice.utils.HelperClass;
import com.app.citycareservice.utils.Params;
import com.app.citycareservice.utils.SharedPrefHandler;
import com.app.citycareservice.utils.roomDB.AddressDatabase;
import com.google.android.material.button.MaterialButton;

import java.net.HttpURLConnection;
import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookServicesActivity extends AppCompatActivity implements Params, AddressSelect, DateSelect, TimeSelect {

    private static final String TAG = "BookServicesActivity";
    private Activity activity;

    private ImageView back_iv;
    private TextView address_tv;
    private TextView change_address_tv;
    private TextView service_time_tv;

    private RecyclerView date_rv;
    private RecyclerView time_rv;

    private MaterialButton book_btn;

    private SharedPrefHandler prefHandler;

    private String time_to_finish;

    private AddressModal addressModal;

    private SelectDateAdapter selectDateAdapter;
    private SelectTimeAdapter selectTimeAdapter;

    private String selectAddress;
    private String selectDate;
    private String selectTime;
    private String serviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_service);

        activity = BookServicesActivity.this;

        back_iv = findViewById(R.id.back_iv);
        address_tv = findViewById(R.id.address_tv);
        change_address_tv = findViewById(R.id.change_address_tv);
        service_time_tv = findViewById(R.id.service_time_tv);
        date_rv = findViewById(R.id.date_rv);
        time_rv = findViewById(R.id.time_rv);
        book_btn = findViewById(R.id.book_btn);

        prefHandler = new SharedPrefHandler(activity);
        selectDateAdapter = new SelectDateAdapter(activity, this);
        try {
            selectTimeAdapter = new SelectTimeAdapter(activity, this);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        AddressDatabase addressDatabase = AddressDatabase.getInstance(activity);
        AddressModal addressModal;

        if (prefHandler.hasKey(SP_KEY_LAST_USED_ADDRESS_ID)) {
            addressModal = addressDatabase.addressDAO().getAddress((prefHandler.getIntFromSharedPref(SP_KEY_LAST_USED_ADDRESS_ID)));
        } else {
            addressModal = addressDatabase.addressDAO().getAddresses().get(0);
        }
        onClick(addressModal);

        change_address_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectAddressBottomSheet(activity, BookServicesActivity.this);
            }
        });

      /*  if (prefHandler.hasKey(SP_KEY_LAST_USED_ADDRESS))
            address_tv.setText(prefHandler.getString(SP_KEY_LAST_USED_ADDRESS));
        else
            change_address_tv.performClick();*/

        Intent intent = getIntent();
//        time_to_finish = intent.getStringExtra(INTENT_KEY_TIME_TO_FINISH");
//        serviceId = intent.getStringExtra(INTENT_KEY_SERVICE_ID");
        time_to_finish = "45";

        service_time_tv.setText("Your Service take aproxx. ".concat(time_to_finish).concat(" mins."));

        date_rv.setAdapter(selectDateAdapter);
        time_rv.setAdapter(selectTimeAdapter);

        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });
    }

    private void validateForm() {

        if (TextUtils.isEmpty(selectAddress)) {
            HelperClass.showToast(activity, "Please Select Address");
            return;
        }

        if (TextUtils.isEmpty(selectDate)) {
            HelperClass.showToast(activity, "Please Select Date");
            return;
        }

        if (TextUtils.isEmpty(selectTime)) {
            HelperClass.showToast(activity, "Please Select Time");
            return;
        }

        bookServiceAPi();
    }

    /*TODO Check Api*/
    private void bookServiceAPi() {

        if (HelperClass.getNetworkInfo(activity)) {
            HelperClass.showLoader(activity);

            SharedPrefHandler prefHandler = new SharedPrefHandler(activity);

            Service api = ApiClient.apiService(activity).create(Service.class);
            Call<SearchServiceResponse> call = api.createOrder(prefHandler.getString(SP_KEY_AUTH_TOKEN), prefHandler.getString(SP_KEY_USER_ID), serviceId, selectDate, selectTime, DEFAULT_EMPTY_STRING, selectAddress);
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

                        HelperClass.showToast(activity, apiResponse.getMessage());
                        finish();

                    } else
                        HelperClass.showToast(activity, activity.getString(R.string.something_went_wrong));

                    HelperClass.hideLoader();
                }

                @Override
                public void onFailure
                        (@NonNull Call<SearchServiceResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: Error is -- " + t);
                    HelperClass.hideLoader();
                    HelperClass.showToast(activity, activity.getString(R.string.something_went_wrong));
                }
            });

        } else
            HelperClass.showToast(activity, activity.getString(R.string.check_internet_connection));

    }

    @Override
    public void onClick(AddressModal addressModal) {
        this.addressModal = addressModal;
        selectAddress = addressModal.getName().concat(" , ")
                .concat(addressModal.getHouseNo()).concat(" , ")
                .concat(addressModal.getLandmark()).concat(" , ")
                .concat(addressModal.getAddress());
        address_tv.setText(selectAddress);
    }


    @Override
    public void onClick(String tag, String date) {
        if (TextUtils.equals(SelectTimeAdapter.class.getSimpleName(), tag))
            selectTime = date;
        else if (TextUtils.equals(SelectDateAdapter.class.getSimpleName(), tag))
            selectDate = date;
    }
}