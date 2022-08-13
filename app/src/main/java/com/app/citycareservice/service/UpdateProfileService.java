package com.app.citycareservice.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.citycareservice.R;
import com.app.citycareservice.interfaces.api.Authentication;
import com.app.citycareservice.modals.authentication.UpdateProfile.Result;
import com.app.citycareservice.modals.authentication.UpdateProfile.UpdateProfileResponse;
import com.app.citycareservice.utils.ApiClient;
import com.app.citycareservice.utils.HelperClass;
import com.app.citycareservice.utils.Params;
import com.app.citycareservice.utils.SharedPrefHandler;

import java.io.File;
import java.net.HttpURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileService extends IntentService implements Params {

    private static final String TAG = "UpdateProfileService";

    private Context context;
    private SharedPrefHandler sharedPrefHandler;

    public UpdateProfileService() {
        super(TAG);
        Log.d(TAG, "UpdateProfileService: created");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: Started");

        context = getApplicationContext();
        sharedPrefHandler = new SharedPrefHandler(context);

        if (intent != null) {
            Bundle bundle = intent.getBundleExtra(BUNDLE_NAME_PROFILE_DATA);
            updateProfile(bundle);
        }

    }

    private void updateProfile(Bundle bundle) {

        if (HelperClass.getNetworkInfo(context)) {

//            RequestBody user_id = null;
            RequestBody user_email = null;
            RequestBody first_name = null;
            RequestBody last_name = null;
            MultipartBody.Part profile_pic = null;

            if (bundle.containsKey(BUNDLE_KEY_PROFILE_PIC_URI)) {
                //has profile image
//                File finalFile = new File(bundle.getString(BUNDLE_KEY_PROFILE_PIC_URI));
                File finalFile = new File(HelperClass.getRealPathFromURI(context, Uri.parse(bundle.getString(BUNDLE_KEY_PROFILE_PIC_URI))));
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), finalFile);
                profile_pic = MultipartBody.Part.createFormData(API_PROFILE_PIC_KEY, finalFile.getName(), requestBody);
            }

//            if (bundle.containsKey(BUNDLE_KEY_USER_ID))
//                user_id = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(BUNDLE_KEY_USER_ID));

            if (bundle.containsKey(BUNDLE_KEY_USER_EMAIL))
                user_email = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(BUNDLE_KEY_USER_EMAIL));

//            TODO Pending
           /* if (bundle.containsKey(BUNDLE_KEY_USER_ADDRESS))
                user_address = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(BUNDLE_KEY_USER_ADDRESS));*/

            if (bundle.containsKey(BUNDLE_KEY_USER_FIRST_NAME))
                first_name = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(BUNDLE_KEY_USER_FIRST_NAME));

            if (bundle.containsKey(BUNDLE_KEY_USER_LAST_NAME))
                last_name = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(BUNDLE_KEY_USER_LAST_NAME));

//            if (bundle.containsKey(BUNDLE_KEY_USER_FULL_NAME))
//                last_name = RequestBody.create(MediaType.parse("text/plain"), bundle.getString(BUNDLE_KEY_USER_LAST_NAME));

            Authentication social = ApiClient.apiService(context).create(Authentication.class);
            Call<UpdateProfileResponse> call = social.updateProfile(sharedPrefHandler.getString(SP_KEY_AUTH_TOKEN),  first_name, last_name, user_email, profile_pic);
            call.enqueue(new Callback<UpdateProfileResponse>() {
                @Override
                public void onResponse(@NonNull Call<UpdateProfileResponse> call, @NonNull Response<UpdateProfileResponse> response) {
                    Log.d(TAG, "onResponse: Response is -- " + response.body());

                    if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                        UpdateProfileResponse apiResponse = response.body();

                        if (response.body().getStatus()) {

                            Result result = apiResponse.getResults().get(0);

                            Bundle bundle = new Bundle();
                            bundle.putString(BUNDLE_KEY_USER_ID, result.getId());
                            bundle.putString(BUNDLE_KEY_USER_FIRST_NAME, result.getFirstName());
                            bundle.putString(BUNDLE_KEY_USER_LAST_NAME, result.getLastName());
                            bundle.putString(BUNDLE_KEY_USER_EMAIL, result.getEmail());
//                            bundle.putString(BUNDLE_KEY_USER_ADDRESS, result.getAddress());
                            bundle.putString(BUNDLE_KEY_PROFILE_PIC_URL, result.getProfilePic());

                            sharedPrefHandler.setUserData(bundle);
                            HelperClass.showToast(context, apiResponse.getMessage());

                            stopSelf();
                        } else
                            HelperClass.showToast(context, apiResponse.getMessage());
                    } else
                        HelperClass.showToast(context, getString(R.string.something_went_wrong));
                }

                @Override
                public void onFailure(@NonNull Call<UpdateProfileResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: Error is -- " + t);
                    HelperClass.showToast(context, t.getMessage());
                }
            });

        } else
            HelperClass.showToast(context, getString(R.string.check_internet_connection));
    }

}