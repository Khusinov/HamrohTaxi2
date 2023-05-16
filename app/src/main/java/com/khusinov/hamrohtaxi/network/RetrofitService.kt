package com.khusinov.hamrohtaxi.network

import com.khusinov.hamrohtaxi.models.AuthLogin
import com.khusinov.hamrohtaxi.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {

    @POST("auth/login/")
    fun login(@Body authLogin: AuthLogin): Call<LoginResponse>

}