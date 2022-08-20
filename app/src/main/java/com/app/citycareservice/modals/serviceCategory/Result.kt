package com.app.citycareservice.modals.serviceCategory

data class Result(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val description: String,
    val image: String,
    val name: String,
    val services: List<Service>,
    val updatedAt: String
)