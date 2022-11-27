package com.app.citycareservice.modals.order.myOrders

data class Service(
    val _id: String,
    val category_id: String,
    val completion_time: String,
    val createdAt: String,
    val description: String,
    val icon: String,
    val image: String,
    val offfers: List<Any>,
    val price: String,
    val rating: String,
    val title: String
)