package com.example.gagalmuluyaallah

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gagalmuluyaallah.response.StoriesItemsResponse

class StoryPagingSource : PagingSource<Int, LiveData<List<StoriesItemsResponse>>>() {

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoriesItemsResponse>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoriesItemsResponse>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(items: List<StoriesItemsResponse>): PagingData<StoriesItemsResponse> {
            return PagingData.from(items)
        }
    }

}