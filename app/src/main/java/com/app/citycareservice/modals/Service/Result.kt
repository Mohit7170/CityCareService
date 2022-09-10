package com.app.citycareservice.modals.Service

data class Result(
    val __v: Int,
    val _id: String,
    val category_id: String,
    val createdAt: String,
    val description: String,
    val icon: String,
    val image: String,
    val title: String,
    val completion_time: String,
    val offfers: List<String>,
    val price: String,
    val rating: String,
//    val updatedAt: String
)