package com.khusinov.hamrohtaxi.models

data class User(
    val id: Int? = 0,
    val last_login: String? = null,
    val is_superuser: Boolean? = false,
    val username: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val email: String? = null,
    val is_staff: Boolean? = false,
    val is_active: Boolean? = false,
    val date_joined: String? = null,
    val name: String? = null,
    val phone_number: String? = null,
    val user_role: Int = 0,
    val groups: Any? = null,
    val user_permissions: Any? = null
)
