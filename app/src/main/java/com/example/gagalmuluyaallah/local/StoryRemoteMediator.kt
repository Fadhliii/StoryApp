package com.example.gagalmuluyaallah.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.gagalmuluyaallah.model.ApiService
import com.example.gagalmuluyaallah.response.StoriesItemsResponse

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
        private val storyDatabase: StoryDatabase,
        private val apiService: ApiService
) : RemoteMediator<Int, StoriesItemsResponse>() {

    override suspend fun load(
            //! LoadType is the type of load that we can use in paging3
            loadType: LoadType,
            //! state is the state of the paging3 that we can use to get the data
            state: PagingState<Int, StoriesItemsResponse>
    ): MediatorResult {
        val page = when (loadType) {
            //? those three loadType is the type of loadType that we can use in paging3//
            //! REFRESH is the type of loadType that will be called when we refresh the data
            LoadType.REFRESH -> getRemoteKeyClosestToCurrentPosition(state)?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            //! PREPEND is the type of loadType that will be called when we scroll up the data
            LoadType.PREPEND -> getRemoteKeyForFirstItem(state)?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = false)
            //! APPEND is the type of loadType that will be called when we scroll down the data
            LoadType.APPEND -> getRemoteKeyForLastItem(state)?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = false)
        }

        return try {
            //! get the data from the api
            val response = apiService.getStories(page, state.config.pageSize)
            val responseData = response.listStory
            val endOfPaginationReached = responseData.isEmpty()
            //! insert the data to the database
            storyDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    storyDatabase.remoteKeysDao().deleteRemoteKeys()
                    storyDatabase.storyDao().deleteAll()
                }
                 //! prevkey is the key of the previous page
                val prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1
                // !nextKey is the key of the next page
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.map { RemoteKeys(it.id, prevKey, nextKey) }

                storyDatabase.remoteKeysDao().insertAll(keys)
                storyDatabase.storyDao().insertStory(responseData)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    //! get the remote key for the first item in the list
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoriesItemsResponse>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            storyDatabase.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }
    //! get the remote key for the last item in the list
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoriesItemsResponse>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            storyDatabase.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    //! get the remote key closest to the current position
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoriesItemsResponse>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                storyDatabase.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}