package com.example.gagalmuluyaallah

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun doRegister(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("password") password: String,
    ): GeneralResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun doLogin(
            @Field("email") email: String,
            @Field("password") password: String
    ): LoginResponse


}