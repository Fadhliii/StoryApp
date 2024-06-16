package com.example.gagalmuluyaallah.Map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gagalmuluyaallah.ResultSealed
import com.example.gagalmuluyaallah.connection.GeneralRepository
import com.example.gagalmuluyaallah.response.StoriesItemsResponse

class MapsViewModel(
        private val repository: GeneralRepository,
) : ViewModel() {

    // ! get stories with location
    fun getStoriesWithLocation(): LiveData<ResultSealed<List<StoriesItemsResponse>>> {
        return repository.getStoriesWithLocation()
    }

}
