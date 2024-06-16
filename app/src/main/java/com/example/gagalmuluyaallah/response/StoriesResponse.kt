package com.example.gagalmuluyaallah.response

import com.google.gson.annotations.SerializedName

data class StoriesResponse(

        @field:SerializedName("listStory")
        val listStory: List<StoriesItemsResponse> = emptyList(),

        @field:SerializedName("error")
        val error: Boolean? = null,

        @field:SerializedName("message")
        val message: String? = null,
)