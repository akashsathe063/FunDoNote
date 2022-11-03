package com.example.fundonote.model

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    var okHttpClient: OkHttpClient? = null
     var retrofitClient: RetrofitClient? = null
    private lateinit var api: MyApi

    init {
        val httpClient = OkHttpClient.Builder()
        okHttpClient =
            httpClient.connectTimeout(40, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS).build()
    }

    fun getInstance(): RetrofitClient? {
        if (retrofitClient == null) {
            retrofitClient = RetrofitClient
        }
        return retrofitClient
    }


    public fun getMyApi(): MyApi {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(MyApi::class.java)
        return api
    }

}