package com.example.gagalmuluyaallah.View

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gagalmuluyaallah.connection.GeneralRepository
import com.example.gagalmuluyaallah.connection.UserPreference
import kotlinx.coroutines.launch

class UserWelcomeViewModel ( //connecting to GeneralRepository and UserPreference
        private val repository: GeneralRepository,
        private val userPreference: UserPreference
): ViewModel() {
    fun logoutMainVM(){ //connecting to userpreference.logout
        viewModelScope.launch {
            userPreference.logout()
        }
    }
//    !!val stories: LiveData<Result<PagingData<StoryItem>>> by lazy {
//    !!    repository.getAllStories(viewModelScope)
//    !! }
}