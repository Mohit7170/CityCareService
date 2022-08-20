package com.app.citycareservice.modals.Service

data class ServiceDetailResponse(
    val message: String,
    val results: List<Result>,
    val status: Boolean,
    val status_code: Int
)