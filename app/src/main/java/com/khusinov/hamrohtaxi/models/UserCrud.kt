package com.khusinov.hamrohtaxi.models

data class UserCrud(
    var name:String? = null,
    var phone_number: String? = null,
    var password: String? = null,
    var user_role:Int = 0
)
