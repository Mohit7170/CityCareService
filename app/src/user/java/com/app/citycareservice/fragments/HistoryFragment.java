package com.app.citycareservice.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.citycareservice.R;
import com.app.citycareservice.adapters.recycler_view.BookingHistoryAdapter;
import com.app.citycareservice.interfaces.order.Service;
import com.app.citycareservice.modals.ServiceModal;
import com.app.citycareservice.modals.historyModal.BookingHistoryResponse;
import com.app.citycareservice.modals.search.service.SearchServiceResponse;
import com.app.citycareservice.utils.ApiClient;
import com.app.citycareservice.utils.HelperClass;
import com.app.citycareservice.utils.Params;
import com.app.citycareservice.utils.SharedPrefHandler;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment implements Params {

    private static final String TAG = "HistoryFragment";
    private Activity activity;

    private ScrollView scrollView;
    private RecyclerView bookings_rv;
    private BookingHistoryAdapter historyAdapter;

    private int page_no = 1;
    private boolean has_more_data = false;

    private final ViewTreeObserver.OnScrollChangedListener scrollPaginationListener = new ViewTreeObserver.OnScrollChangedListener() {
        @Override
        public void onScrollChanged() {
            View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                    .getScrollY()));
            if (diff == 0) {
                if (has_more_data) {
                    page_no++;
                    getMyOrders();
                }
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (Activity) view.getContext();

        HelperClass.changeStatusBarColor(activity, R.color.white, true);

        scrollView = view.findViewById(R.id.history_sv);
        bookings_rv = view.findViewById(R.id.bookings_rv);
        historyAdapter = new BookingHistoryAdapter(activity);

        bookings_rv.setAdapter(historyAdapter);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(scrollPaginationListener);

        getMyOrders();

    }

    /*TODO Check Api*/
    private void getMyOrders() {

        if (HelperClass.getNetworkInfo(activity)) {
            HelperClass.showLoader(activity);

            SharedPrefHandler prefHandler = new SharedPrefHandler(activity);

            Service api = ApiClient.apiService(activity).create(Service.class);
            Call<SearchServiceResponse> call = api.getMyOrders(prefHandler.getString(SP_KEY_AUTH_TOKEN), page_no);
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

//                        has_more_data = apiResponse.getTotal_pages() > page_no;

                        if (apiResponse.getStatus()) {

                            List<BookingHistoryResponse> serviceModals = new ArrayList<>();
                            if (page_no == 1)
                                historyAdapter.setData(serviceModals);
                            else
                                historyAdapter.addData(serviceModals);

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