package com.example.gagalmuluyaallah

import com.example.gagalmuluyaallah.response.StoriesItemsResponse


object DataDummy {
    fun generateDummyStoryResponse(): List<StoriesItemsResponse> {
        val items = mutableListOf<StoriesItemsResponse>()
        for (i in 0..100) {
            val story = StoriesItemsResponse(
                    // ! this is the dummy data for the story item model class
                    "id $i",
                    "www.photo-images.com/$i",
                    "1-1-$i",
                    "name $i",
                    "description $i",
                    i + 1.0,
                    i + 2.0)
            items.add(story)
        }
        return items
    }
}