package com.example.gagalmuluyaallah

import androidx.lifecycle.ViewModel

// this viewmodel is for register so it can split the ui and model
class RegisterViewModel(val repository: GeneralRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) = repository.register(name, email, password)

}
