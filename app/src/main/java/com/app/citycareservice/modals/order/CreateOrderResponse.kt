package com.app.citycareservice.modals.order

data class CreateOrderResponse(
    val message: String,
    val results: List<Result>,
    val status: Boolean,
    val status_code: Int
)