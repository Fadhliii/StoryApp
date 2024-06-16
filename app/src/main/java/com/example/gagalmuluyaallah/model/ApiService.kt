package com.example.gagalmuluyaallah.model

import com.example.gagalmuluyaallah.response.GeneralResponse
import com.example.gagalmuluyaallah.response.LoginResponse
import com.example.gagalmuluyaallah.response.StoriesResponse
import com.example.gagalmuluyaallah.response.StoryItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    // ! Get stories with location
    @GET("stories")
    suspend fun getStoriesWithLocation(
            @Query("page") page: Int = 1,
            @Query("size") size: Int = 25,
            @Query("location") location: Int = 1,
    ): StoriesResponse

    // ! Register function
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("password") password: String,
    ): GeneralResponse

    // ! Login function
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
            @Field("email") email: String,
            @Field("password") password: String,
    ): LoginResponse


    // ! Add new story
    @Multipart
    @POST("stories")
    suspend fun addNewStory(
            @Part file: MultipartBody.Part,
            @Part("description") description: RequestBody,
            @Part("lat") lat: Double?,
            @Part("lon") lon: Double?,
    ): GeneralResponse

    // ! Get stories List function
    @GET("stories")
    suspend fun getStories(
            @Query("page") page: Int = 1,
            @Query("size") size: Int = 25,
    ): StoriesResponse


}