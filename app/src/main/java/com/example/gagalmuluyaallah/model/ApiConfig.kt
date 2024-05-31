package com.example.gagalmuluyaallah.model


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiConfig {
    fun getApiService(token: String): ApiService {
        val okhttp = OkHttpClient.Builder()
            .apply {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(loggingInterceptor)

                addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token") // tambahkan header untuk token login
                        .build()
                    chain.proceed(request)
                }
            }
            .readTimeout(25, TimeUnit.SECONDS) // 25 detik
            .writeTimeout(150, TimeUnit.SECONDS) // 2,5 menit
            .connectTimeout(60, TimeUnit.SECONDS) // 1 menit
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create<ApiService>() // lebih singkat dan bersih generic
    }
}