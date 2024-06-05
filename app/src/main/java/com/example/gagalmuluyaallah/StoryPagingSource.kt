//package com.example.gagalmuluyaallah
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.gagalmuluyaallah.model.ApiService
//import com.example.gagalmuluyaallah.model.ListStoryItem
//
//class StoryPagingSource(
//        private val apiService: ApiService
//) : PagingSource<Int, ListStoryItem>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
//        return try {
//            val page = params.key ?: 1
//            val response = apiService.getStories(page)
//            LoadResult.Page(
//                    data =  response.ListStoryItem!!,
//                    prevKey = if (page == 1) null else page - 1,
//                    nextKey = null // We don't know if there are more pages, so we set this to null
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
//        return state.anchorPosition
//    }
//}