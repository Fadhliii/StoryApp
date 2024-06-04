package com.example.gagalmuluyaallah.model

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("password") password: String,
    ): GeneralResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
            @Field("email") email: String,
            @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
            @Part file: MultipartBody.Part,
            @Part("description") description: RequestBody,
            @Part("lat") lat: Double?,
            @Part("lon") lon: Double?
    ): GeneralResponse


}