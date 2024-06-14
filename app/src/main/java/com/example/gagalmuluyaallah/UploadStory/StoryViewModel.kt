package com.example.gagalmuluyaallah.UploadStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.gagalmuluyaallah.ResultSealed
import com.example.gagalmuluyaallah.connection.GeneralRepository
import com.example.gagalmuluyaallah.response.StoryItem

class StoryViewModel(
        private val repository: GeneralRepository,
) : ViewModel() {
    val stories: LiveData<ResultSealed<PagingData<StoryItem>>> by lazy {
        repository.getAllStories(viewModelScope)
    }

}