package com.app.citycareservice.ui.activities.service;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.adapters.recycler_view.SearchAdapter;
import com.app.citycareservice.interfaces.api.Authentication;
import com.app.citycareservice.modals.search.service.SearchServiceResponse;
import com.app.citycareservice.utils.ApiClient;
import com.app.citycareservice.utils.HelperClass;
import com.app.citycareservice.utils.Params;
import com.app.citycareservice.utils.SharedPrefHandler;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements Params {

    private static final String TAG = "SearchActivity";

    private Activity activity;
    private RecyclerView search_rv;
    private SearchView searchView;
    private ImageView back_iv;

    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        activity = SearchActivity.this;
        search_rv = findViewById(R.id.search_rv);
        searchView = findViewById(R.id.search);
        back_iv = findViewById(R.id.back_iv);

        searchAdapter = new SearchAdapter(activity);
        search_rv.setAdapter(searchAdapter);


        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!TextUtils.isEmpty(query))
                    searchAPi(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }

    private void searchAPi(String query) {

        if (HelperClass.getNetworkInfo(activity)) {
            HelperClass.showLoader(activity);

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

                            searchAdapter.setData(apiResponse.getResults());

                        } else
                            HelperClass.showToast(activity, apiResponse.getMessage());
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

}