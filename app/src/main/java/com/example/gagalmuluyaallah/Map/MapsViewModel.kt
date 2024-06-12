package com.example.gagalmuluyaallah.Map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gagalmuluyaallah.ResultSealed
import com.example.gagalmuluyaallah.connection.GeneralRepository
import com.example.gagalmuluyaallah.response.StoryItem

class MapsViewModel(
        private val repository: GeneralRepository,
) : ViewModel() {
    fun getStoriesWithLocation(): LiveData<ResultSealed<List<StoryItem>>> {
        return repository.getStoriesWithLocation()
    }

}
