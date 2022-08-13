package com.app.citycareservice.modals.allService

data class Result(
    val _id: String,
    val name: String,
    val services: List<Service>
)