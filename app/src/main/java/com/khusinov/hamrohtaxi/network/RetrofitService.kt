package com.khusinov.hamrohtaxi.network

import com.khusinov.hamrohtaxi.models.*
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    @POST("auth/login/")
    fun login(@Body authLogin: AuthLogin): Call<LoginResponse>

    @POST("auth/signup/")
    fun signup(@Body userCrud: UserCrud ): Call<LoginResponse>

    @POST("cars/")
    fun createCar(@Body car: Car, @Header("Authorization") token: String):Call<CarResponse>

    @GET("posts-list/")
    fun getAllPosts():Call<MyPosts>

    @GET("regions-list/")
    fun getRegions():Call<List<Region>>

    @GET("districts-list/")
    fun getDistricts():Call<List<District>>

    @GET("posts/")
    fun getMyPosts(@Header("Authorization") token: String):Call<MyPosts>

    @POST("posts/")
    fun createPost(@Body post2: Post2 , @Header("Authorization") token: String):Call<Post3>

    @DELETE("posts/{id}/")
    fun deletePostById(@Path("id") id:Int , @Header("Authorization") token: String):Call<DeleteResponse>

    @PUT("posts/{id}/")
    fun updatePostById(@Path("id") id:Int, @Body post2: Post2 , @Header("Authorization") token:String):Call<Post3>







}