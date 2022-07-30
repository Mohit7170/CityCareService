package com.app.citycareservice.interfaces.order;

import com.app.citycareservice.modals.search.service.SearchServiceResponse;
import com.app.citycareservice.utils.Params;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Service extends Params {

    @FormUrlEncoded
    @POST(API_CREATE_ORDER_URL)
    Call<SearchServiceResponse> createOrder(@Header(API_AUTH_TOKEN_KEY) String auth_token,
                                            @Field(API_SERVICE_ID_KEY) String user_id,
                                            @Field(API_SERVICE_ID_KEY) String service_id,
                                            @Field(API_SERVICE_DATE_KEY) String date,
                                            @Field(API_SERVICE_TIME_KEY) String time,
                                            @Field(API_SERVICE_REMARKS_KEY) String remarks,
                                            @Field(API_SERVICE_LOCATION_KEY) String address);

    @FormUrlEncoded
    @POST(API_MY_ORDERS_URL)
    Call<SearchServiceResponse> getMyOrders(@Header(API_AUTH_TOKEN_KEY) String auth_token,
                                            @Field(API_PAGE_NUMBER_KEY) int page_number);


    @FormUrlEncoded
    @POST(API_GET_ALL_SERVICE_URL)
    Call<SearchServiceResponse> getAllServices(@Header(API_AUTH_TOKEN_KEY) String auth_token);

}
