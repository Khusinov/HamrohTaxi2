package com.khusinov.hamrohtaxi.models

data class LoginResponse(
    val refresh: String? = null,
    val access: String? = null,
    val user: User
)
