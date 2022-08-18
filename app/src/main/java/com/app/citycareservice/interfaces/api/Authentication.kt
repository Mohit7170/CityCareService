package com.app.citycareservice.interfaces.api

import com.app.citycareservice.modals.authentication.allUserData.UserDetailResponse
import com.app.citycareservice.modals.search.service.SearchServiceResponse
import com.app.citycareservice.utils.Params
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Authentication : Params {
    @FormUrlEncoded
    @POST(Params.API_REGISTER_URL)
    fun checkRegister(@Field(Params.API_PHONE_NUM_KEY) user_phone: String): Call<UserDetailResponse>

    @FormUrlEncoded
    @POST(Params.API_SERVICE_URL)
    fun serviceSearch(
        @Header(Params.API_AUTH_TOKEN_KEY) auth_token: String,
        @Field(Params.API_QUERY_KEY) query: String? = null
    ): Call<SearchServiceResponse>

    @Multipart
    @POST(Params.API_UPDATE_PROFILE_URL)
    fun updateProfile(
        @Header(Params.API_AUTH_TOKEN_KEY) authToken: String,
        @Part(Params.API_NAME_KEY) fullName: RequestBody? = null,
        @Part(Params.API_EMAIL_KEY) emailId: RequestBody? = null,
        @Part profilePic: MultipartBody.Part? = null
    ): Call<UserDetailResponse>
}