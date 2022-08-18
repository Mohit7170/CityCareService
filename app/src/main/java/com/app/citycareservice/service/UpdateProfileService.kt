package com.app.citycareservice.service

import android.app.IntentService
import android.content.Context
import com.app.citycareservice.utils.SharedPrefHandler
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.app.citycareservice.utils.HelperClass
import okhttp3.RequestBody
import com.app.citycareservice.interfaces.api.Authentication
import com.app.citycareservice.utils.ApiClient
import com.app.citycareservice.modals.authentication.allUserData.UserDetailResponse
import com.app.citycareservice.R
import com.app.citycareservice.utils.Params
import com.app.citycareservice.utils.Params.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.HttpURLConnection

class UpdateProfileService : IntentService(TAG), Params {
    private var context: Context? = null
    private var sharedPrefHandler: SharedPrefHandler? = null
    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent: Started")
        context = applicationContext
        sharedPrefHandler = SharedPrefHandler(context)
        if (intent != null) {
            val bundle = intent.getBundleExtra(Params.BUNDLE_NAME_PROFILE_DATA)
            updateProfile(bundle)
        }
    }

    private fun updateProfile(bundle: Bundle?) {
        if (HelperClass.getNetworkInfo(context)) {
            var profilePic: MultipartBody.Part? = null
            if (bundle!!.containsKey(Params.BUNDLE_KEY_PROFILE_PIC_URI)) {
                val finalFile = File(
                    HelperClass.getRealPathFromURI(
                        context, Uri.parse(
                            bundle.getString(Params.BUNDLE_KEY_PROFILE_PIC_URI)
                        )
                    )
                )

                profilePic = MultipartBody.Part.createFormData(
                    Params.API_PROFILE_PIC_KEY,
                    finalFile.name,
                    finalFile.asRequestBody("image/*".toMediaType())
                )
            }

            val social = ApiClient.apiService(context).create(
                Authentication::class.java
            )
            val call = social.updateProfile(
                authToken = sharedPrefHandler!!.getString(Params.SP_KEY_AUTH_TOKEN),
                fullName = bundle.getString(BUNDLE_KEY_USER_FULL_NAME)?.toRequestBody() ?: null,
                emailId = bundle.getString(BUNDLE_KEY_USER_EMAIL)?.toRequestBody(),
                profilePic
            )
            call.enqueue(object : Callback<UserDetailResponse?> {
                override fun onResponse(
                    call: Call<UserDetailResponse?>,
                    response: Response<UserDetailResponse?>
                ) {
                    Log.d(TAG, "onResponse: Response is -- " + response.body())
                    if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                        val apiResponse = response.body()
                        if (response.body()!!.status) {
                            val (_, _, _, _, _, email, id, _, _, name, _, profile_pic1) = apiResponse!!.results[0]
                            val bundle = Bundle()
                            bundle.putString(BUNDLE_KEY_USER_ID, id)
                            bundle.putString(BUNDLE_KEY_USER_FULL_NAME, name)
                            bundle.putString(BUNDLE_KEY_USER_EMAIL, email)
                            bundle.putString(BUNDLE_KEY_PROFILE_PIC_URL, profile_pic1)
                            sharedPrefHandler!!.setUserData(bundle)
                            HelperClass.showToast(context, apiResponse.message)
                            stopSelf()
                        } else HelperClass.showToast(context, apiResponse!!.message)
                    } else HelperClass.showToast(context, getString(R.string.something_went_wrong))
                }

                override fun onFailure(call: Call<UserDetailResponse?>, t: Throwable) {
                    Log.d(TAG, "onFailure: Error is -- $t")
                    HelperClass.showToast(context, t.message)
                }
            })
        } else HelperClass.showToast(context, getString(R.string.check_internet_connection))
    }

    companion object {
        private const val TAG = "UpdateProfileService"
    }

    init {
        Log.d(TAG, "UpdateProfileService: created")
    }
}