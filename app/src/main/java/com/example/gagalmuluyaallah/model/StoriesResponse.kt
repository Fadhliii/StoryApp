package com.example.gagalmuluyaallah.model

import StoryItem
import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoriesResponse(

        @field:SerializedName("listStory")
        val listStory: List<StoryItem> = emptyList(),

        @field:SerializedName("error")
        val error: Boolean? = null,

        @field:SerializedName("message")
        val message: String? = null,
)