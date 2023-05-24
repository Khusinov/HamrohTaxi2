package com.khusinov.hamrohtaxi.network

import com.khusinov.hamrohtaxi.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RetrofitService {

    @POST("auth/login/")
    fun login(@Body authLogin: AuthLogin): Call<LoginResponse>

    @POST("auth/signup/")
    fun signup(@Body userCrud: UserCrud ): Call<LoginResponse>

    @POST("cars/")
    fun createCar(@Body car: Car, @Header("Authorization") token: String):Call<CarResponse>

    @GET("posts-list/")
    fun getAllPosts():Call<List<Post>>

    @GET("regions-list/")
    fun getRegions():Call<List<Region>>

    @GET("districts-list/")
    fun getDistricts():Call<List<District>>

    @GET("posts/")
    fun getMyPosts(@Header("Authorization") token: String):Call<MyPosts>








}