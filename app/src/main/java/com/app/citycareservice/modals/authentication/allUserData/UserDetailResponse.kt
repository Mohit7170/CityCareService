package com.app.citycareservice.modals.authentication.allUserData

data class UserDetailResponse(
    val message: String,
    val results: List<Result>,
    val status: Boolean,
    val status_code: Int
)