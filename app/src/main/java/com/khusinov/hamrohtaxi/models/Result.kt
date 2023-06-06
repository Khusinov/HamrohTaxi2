package com.khusinov.hamrohtaxi.models

data class Result(
    val addition: String,
    val count: Int,
    val from_location: String,
    val go_time: String,
    val id: Int,
    val posted_time: String,
    val price: String,
    val status: Int,
    val to_location: String,
    val user: UserX,
    val user_role: Int
)