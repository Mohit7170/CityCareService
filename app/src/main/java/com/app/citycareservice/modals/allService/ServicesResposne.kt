package com.app.citycareservice.modals.allService

data class ServicesResposne(
    val message: String,
    val page_no: Int,
    val results: List<Result>,
    val status: Boolean,
    val status_code: Int,
    val total_pages: Int
)