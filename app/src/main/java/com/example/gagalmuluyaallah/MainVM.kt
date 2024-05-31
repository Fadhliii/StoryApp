package com.example.gagalmuluyaallah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainVM ( //connecting to GeneralRepository and UserPreference
        private val repository: GeneralRepository,
        private val userPreference: UserPreference
): ViewModel() {
    fun logoutMainVM(){ //connecting to userpreference.logout
        viewModelScope.launch {
            userPreference.userLogout()
        }
    }
}