package com.example.gagalmuluyaallah


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiConfig {
    private val okhttp = OkHttpClient.Builder()
        .apply {
            // logging interceptor is for logging the request and response of the API in the logcat
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(loggingInterceptor)
        }
        .readTimeout(25, TimeUnit.SECONDS) // 25 seconds
        .writeTimeout(300, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    // retrofit is for the retrofit builder to build the retrofit object to access the API endpoints
    val retrofit = Retrofit.Builder()
        .baseUrl("https://story-api.dicoding.dev/v1")
        .client(okhttp) // client is for the okhttp
        .addConverterFactory(
                GsonConverterFactory.create()) // converter is for the gson converter factory
        // to convert the response of the API to the data class that we have created in the model
        .build()

    val getApiService = retrofit.create<ApiService>() // lebih singkat dan bersih generic
}