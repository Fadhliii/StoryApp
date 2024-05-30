package com.example.gagalmuluyaallah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VMFactory (
        private val repository: GeneralRepository,
        private val userPreference: UserPreference
) : ViewModelProvider.Factory {
}