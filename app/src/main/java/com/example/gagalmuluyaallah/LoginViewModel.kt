package com.example.gagalmuluyaallah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(
        //summon repository and preference to get the token <3
        private val repository: GeneralRepository,
        private val pref: UserPreference,
) : ViewModel() {
    fun loginViewModel(email: String, password: String) = repository.login(email, password) // not yet to be implemented

    //save login session
    fun saveLoginViewModel(token: String) { // not yet to be implemented
        viewModelScope.launch {
            pref.saveToken(token)
            //generate login
            pref.login()
        }

    }
    suspend fun logout() {
        // Panggil fungsi logout di UserPreference
        pref.logout()
    }
}
