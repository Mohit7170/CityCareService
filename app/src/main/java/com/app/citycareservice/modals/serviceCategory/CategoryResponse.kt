package com.app.citycareservice.modals.serviceCategory

data class CategoryResponse(
    val message: String,
    val results: List<Result>,
    val status: Boolean,
    val status_code: Int
)