package com.app.citycareservice.modals.order.myOrders

import com.app.citycareservice.utils.CommonEnum

data class Result(
    val _id: String,
    val createdAt: String,
    val location: String,
    val price: String,
    val remarks: String,
    val service_date: String,
    val service_id: String,
    val service_time: String,
    val services: List<Service>,
    val user: List<User>,
    val user_id: String,
    val review: Review? = null,
   val status: CommonEnum.Status
)

data class Review(val rate:String,val review:String)