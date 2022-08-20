package com.app.citycareservice.interfaces.order

import com.app.citycareservice.modals.Service.ServiceDetailResponse
import com.app.citycareservice.modals.serviceCategory.CategoryResponse
import com.app.citycareservice.modals.allService.ServicesResposne
import com.app.citycareservice.modals.order.CreateOrderResponse
import com.app.citycareservice.modals.order.myOrders.MyOrdersResponse
import com.app.citycareservice.utils.Params
import retrofit2.Call
import retrofit2.http.*

interface Service : Params {
    @FormUrlEncoded
    @POST(Params.API_CREATE_ORDER_URL)
    fun createOrder(
        @Header(Params.API_AUTH_TOKEN_KEY) authToken: String,
        @Field(Params.API_ID_KEY) userId: String,
        @Field(Params.API_SERVICE_ID_KEY) serviceId: String,
        @Field(Params.API_SERVICE_DATE_KEY) serviceDate: String,
        @Field(Params.API_SERVICE_TIME_KEY) serviceTime: String,
        @Field(Params.API_SERVICE_REMARKS_KEY) remarks: String,
        @Field(Params.API_SERVICE_LOCATION_KEY) address: String
    ): Call<CreateOrderResponse>

    //    @FormUrlEncoded
    @POST(Params.API_MY_ORDERS_URL)
    fun getMyOrders(
        @Header(Params.API_AUTH_TOKEN_KEY) authToken: String,
        /*     @Field(Params.API_PAGE_NUMBER_KEY) pageNumber: Int*/
    ): Call<MyOrdersResponse>

    //    @FormUrlEncoded
    @POST(Params.API_GET_ALL_SERVICE_URL)
    fun getAllServices(@Header(Params.API_AUTH_TOKEN_KEY) authToken: String): Call<ServicesResposne>

    @GET("${Params.API_GET_ALL_SERVICE_URL}/{service_id}")
    fun getServiceDetails(
        @Header(Params.API_AUTH_TOKEN_KEY) authToken: String,
        @Path(value = "service_id", encoded = true) serviceId: String
    ): Call<ServiceDetailResponse>

    @GET("${Params.API_CATEGORY_URL}/{category_id}")
    fun getCategoryDetail(
        @Header(Params.API_AUTH_TOKEN_KEY) authToken: String,
        @Path(value = "category_id", encoded = true) categoryId: String
    ): Call<CategoryResponse>

}