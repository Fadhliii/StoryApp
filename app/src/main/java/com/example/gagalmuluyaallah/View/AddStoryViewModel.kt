package com.example.gagalmuluyaallah.View

import androidx.lifecycle.ViewModel
import com.example.gagalmuluyaallah.GeneralRepository
import java.io.File

class AddStoryViewModel(private val repository: GeneralRepository): ViewModel() {

    fun AddNewStory(file: File?, description: String, lat: Double?, lon: Double?) =
            repository.uploadNewStory(file, description, lat, lon)

}