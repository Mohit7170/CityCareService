package com.app.citycareservice.interfaces.api;

import com.app.citycareservice.modals.authentication.Register.RegisterResponse;
import com.app.citycareservice.modals.authentication.allUserData.UserDetailResponse;
import com.app.citycareservice.modals.search.service.SearchServiceResponse;
import com.app.citycareservice.utils.Params;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Authentication extends Params {

    @FormUrlEncoded
    @POST(API_REGISTER_URL)
    Call<UserDetailResponse> checkRegister(@Field(API_PHONE_NUM_KEY) String user_phone);

    @FormUrlEncoded
    @POST(API_SERVICE_URL)
    Call<SearchServiceResponse> serviceSearch(@Header(API_AUTH_TOKEN_KEY) String auth_token,
                                              @Field(API_QUERY_KEY) String query);

    @Multipart
    @POST(API_UPDATE_PROFILE_URL)
    Call<UserDetailResponse> updateProfile(@Header(API_AUTH_TOKEN_KEY) String authToken,
                                           @Part(API_NAME_KEY) RequestBody name,
                                           @Part(API_EMAIL_KEY) RequestBody emailId,
                                           @Part MultipartBody.Part profilePic);
}