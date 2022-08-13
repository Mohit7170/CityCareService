package com.app.citycareservice.fragments

import android.app.Activity
import com.app.citycareservice.adapters.recycler_view.BookingHistoryAdapter
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.app.citycareservice.R
import com.app.citycareservice.databinding.FragmentHistoryBinding
import com.app.citycareservice.utils.HelperClass
import com.app.citycareservice.utils.SharedPrefHandler
import com.app.citycareservice.utils.ApiClient
import com.app.citycareservice.modals.order.myOrders.MyOrdersResponse
import com.app.citycareservice.interfaces.order.Service
import com.app.citycareservice.utils.Params
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class HistoryFragment : Fragment(), Params {

    private lateinit var activity: Activity
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var historyAdapter: BookingHistoryAdapter
    private var pageNo = 1
    private val hasMoreData = false

    private val scrollPaginationListener = OnScrollChangedListener {
        with(binding) {
            val view = historySv.getChildAt(historySv.childCount - 1) as View
            val diff = view.bottom - (historySv.height + historySv.scrollY)
            if (diff == 0) {
                if (hasMoreData) {
                    pageNo++
                    myOrders()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        activity = binding.root.context as Activity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HelperClass.changeStatusBarColor(activity, R.color.white, true)


//        binding.historySv.viewTreeObserver.addOnScrollChangedListener(scrollPaginationListener)

        myOrders()
    }

    private fun myOrders() {
        if (HelperClass.getNetworkInfo(activity)) {
            HelperClass.showLoader(activity)
            val prefHandler = SharedPrefHandler(activity)
            val api = ApiClient.apiService(activity).create(Service::class.java)
            val call = api.getMyOrders(prefHandler.getString(Params.SP_KEY_AUTH_TOKEN) /*page_no*/)
            call.enqueue(object : Callback<MyOrdersResponse> {
                override fun onResponse(call: Call<MyOrdersResponse>, response: Response<MyOrdersResponse>) {
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        //Token Expired
                        HelperClass.logout(activity, true)
                        return
                    }
                    val apiResponse = response.body()
                    if (response.code() == HttpURLConnection.HTTP_OK && apiResponse != null) {

//                        has_more_data = apiResponse.getTotal_pages() > page_no;
                        if (apiResponse.status) {

                            historyAdapter = BookingHistoryAdapter(activity,apiResponse.results)
                            binding.bookingsRv.adapter = historyAdapter


                           /* if (pageNo == 1) historyAdapter.setData(apiResponse.results)
                            else historyAdapter.addData(apiResponse.results)*/
                        } else HelperClass.showToast(activity, apiResponse.message)
                    } else HelperClass.showToast(activity, activity.getString(R.string.something_went_wrong))
                    HelperClass.hideLoader()
                }

                override fun onFailure(call: Call<MyOrdersResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: Error is -- $t")
                    HelperClass.hideLoader()
                    HelperClass.showToast(activity, activity!!.getString(R.string.something_went_wrong))
                }
            })
        } else HelperClass.showToast(activity, activity.getString(R.string.check_internet_connection))
    }

    companion object {
        private const val TAG = "HistoryFragment"
    }
}