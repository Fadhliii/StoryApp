package com.example.gagalmuluyaallah.View

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gagalmuluyaallah.connection.GeneralRepository
import com.example.gagalmuluyaallah.connection.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(
        //summon repository and preference to get the token <3
        private val repository: GeneralRepository,
        private val pref: UserPreference,
) : ViewModel() {
    fun login(email: String, password: String) = repository.login(email, password) // not yet to be implemented

    //save login session
    fun saveLoginState(token: String) { // not yet to be implemented
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
