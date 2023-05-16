package com.khusinov.hamrohtaxi.network

object Common {

    private const val BASE_URL = "http://185.105.91.162/api/"
    val retrofitServices: RetrofitService
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitService::class.java)

}