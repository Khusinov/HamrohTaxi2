package com.khusinov.hamrohtaxi.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null

    val client = OkHttpClient.Builder()
        .hostnameVerifier { _, _ -> true }
        .build()

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {

            retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
        return retrofit!!
    }
}