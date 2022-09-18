package com.app.citycareservice.modals.authentication.allUserData

import com.app.citycarepartner.modals.common.DocumentsData

data class Result(
    val __v: Int,
    val _id: String,
    val auth_token: String,
    val createdAt: String,
    val documents_data: DocumentsData,
    val email: String,
    val id: String,
    val is_available: Boolean,
    val is_verified: Boolean,
    val name: String,
    val phone: String,
    val profile_pic: String,
    val role: String,
    val services: List<Any>,
    val updatedAt: String,
    val user_activated: Boolean
)