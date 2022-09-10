package com.app.citycareservice.ui.activities.service

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.app.citycareservice.R
import com.app.citycareservice.databinding.ActivityServiceDetailBinding
import com.app.citycareservice.interfaces.order.Service
import com.app.citycareservice.modals.Service.ServiceDetailResponse
import com.app.citycareservice.utils.ApiClient
import com.app.citycareservice.utils.HelperClass
import com.app.citycareservice.utils.Params
import com.app.citycareservice.utils.Params.INTENT_KEY_SERVICE_ID
import com.app.citycareservice.utils.SharedPrefHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class ServiceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceDetailBinding
    private lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@ServiceDetailActivity

        binding.backBtn.setOnClickListener { onBackPressed() }

        getCategoryDetails()
        try {
            binding.bookNowBtn.setOnClickListener {
                startActivity(
                    Intent(
                        activity,
                        BookServicesActivity::class.java
                    ).putExtra(
                        INTENT_KEY_SERVICE_ID, intent.getStringExtra(INTENT_KEY_SERVICE_ID)
                            ?: throw Exception("Service Id can't be null")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCategoryDetails() {
        if (HelperClass.getNetworkInfo(activity)) {
            try {
                HelperClass.showLoader(activity)
                val prefHandler = SharedPrefHandler(activity)
                val api = ApiClient.apiService(activity).create(Service::class.java)
                val call =
                    api.getServiceDetails(
                        prefHandler.getString(Params.SP_KEY_AUTH_TOKEN),
                        serviceId = intent.getStringExtra(INTENT_KEY_SERVICE_ID)
                            ?: throw Exception("Service Category Id can't be null")
                    )
                call.enqueue(object : Callback<ServiceDetailResponse> {
                    override fun onResponse(
                        call: Call<ServiceDetailResponse>,
                        response: Response<ServiceDetailResponse>
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

                            if (apiResponse.results.isNotEmpty()) {

                                val data = apiResponse.results[0]

                                with(binding) {

                                    if (apiResponse.status) {

                                        HelperClass.setImage(
                                            activity,
                                            data.icon,
                                            serviceIv,
                                            R.drawable.ic_placeholder
                                        )

                                        ratingTv.text = data.rating
                                        serviceNameTv.text = data.title
                                        detailTv.text = data.description
                                        priceTv.text = "₹ ${data.price}"

                                        /*     servicesRv.adapter =
                                                 ServicesDetailAdapter(activity, data.services)
     */
                                    }

                                }

                                /* if (pageNo == 1) historyAdapter.setData(apiResponse.results)
                             else historyAdapter.addData(apiResponse.results)*/
                            } else HelperClass.showToast(activity, apiResponse.message)
                        } else HelperClass.showToast(
                            activity,
                            activity.getString(R.string.something_went_wrong)
                        )
                    }

                    override fun onFailure(call: Call<ServiceDetailResponse>, t: Throwable) {
                        Log.d(TAG, "onFailure: Error is -- $t")
                        HelperClass.hideLoader()
                        HelperClass.showToast(
                            activity,
                            activity.getString(R.string.something_went_wrong)
                        )
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else HelperClass.showToast(
            activity,
            activity.getString(R.string.check_internet_connection)
        )
    }

    companion object {
        private const val TAG = "ServiceCategoryActivity"
    }
}