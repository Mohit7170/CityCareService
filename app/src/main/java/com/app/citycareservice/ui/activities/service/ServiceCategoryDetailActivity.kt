package com.app.citycareservice.ui.activities.service

import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.app.citycareservice.R
import com.app.citycareservice.adapters.recycler_view.ServicesDetailAdapter
import com.app.citycareservice.databinding.ActivityServiceCategoryDetailBinding
import com.app.citycareservice.interfaces.order.Service
import com.app.citycareservice.modals.serviceCategory.CategoryResponse
import com.app.citycareservice.utils.ApiClient
import com.app.citycareservice.utils.HelperClass
import com.app.citycareservice.utils.Params
import com.app.citycareservice.utils.Params.INTENT_KEY_SERVICE_CATEGORY_ID
import com.app.citycareservice.utils.SharedPrefHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class ServiceCategoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceCategoryDetailBinding
    private lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@ServiceCategoryDetailActivity

        binding.backBtn.setOnClickListener { onBackPressed() }

        getCategoryDetails()
    }

    private fun getCategoryDetails() {
        if (HelperClass.getNetworkInfo(activity)) {
            try {
                HelperClass.showLoader(activity)
                val prefHandler = SharedPrefHandler(activity)
                val api = ApiClient.apiService(activity).create(Service::class.java)
                val call =
                    api.getCategoryDetail(
                        prefHandler.getString(Params.SP_KEY_AUTH_TOKEN),
                        categoryId = intent.getStringExtra(INTENT_KEY_SERVICE_CATEGORY_ID)
                            ?: throw Exception("Service Category Id can't be null")
                    )
                call.enqueue(object : Callback<CategoryResponse> {
                    override fun onResponse(
                        call: Call<CategoryResponse>,
                        response: Response<CategoryResponse>
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
                                            data.image,
                                            serviceIv,
                                            R.drawable.ic_placeholder
                                        )

                                        serviceNameTv.text = data.name
                                        detailTv.text = data.description
                                        ratingTv.text = data.rating

                                        servicesRv.adapter =
                                            ServicesDetailAdapter(activity, data.services)

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

                    override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
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