package com.example.gagalmuluyaallah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(
        //summon repository and preference to get the token <3
        private val repository: GeneralRepository,
        private val userPreference: UserPreference,
) : ViewModel() {
    fun loginViewModel(email: String, password: String) = repository.login(email, password) // not yet to be implemented

    //save login session
    fun saveLoginViewModel(token: String) { // not yet to be implemented
        viewModelScope.launch {
            userPreference.saveToken(token)
            //generate login
            userPreference.onLogin()
        }

    }
}
