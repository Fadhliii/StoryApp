package com.example.gagalmuluyaallah.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gagalmuluyaallah.response.StoriesItemsResponse

@Dao
interface StoryDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoriesItemsResponse>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, StoriesItemsResponse>

    @Query("DELETE FROM story")
    suspend fun deleteAll()

}