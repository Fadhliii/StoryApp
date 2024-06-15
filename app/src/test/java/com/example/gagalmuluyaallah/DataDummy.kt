package com.example.gagalmuluyaallah

import com.example.gagalmuluyaallah.response.StoryItem


object DataDummy {
    fun generateDummyStoryResponse(): List<StoryItem> {
        val items = mutableListOf<StoryItem>()
        for (i in 0..100) {
            val story = StoryItem(
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