package com.edudev.finai.domain.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val imageUrl: String? = null
)
