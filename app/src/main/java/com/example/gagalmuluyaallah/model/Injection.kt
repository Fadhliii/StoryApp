@file:Suppress("DEPRECATION")

package com.example.gagalmuluyaallah.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.gagalmuluyaallah.GeneralRepository
import com.example.gagalmuluyaallah.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")
    fun provideRepository(context: Context): GeneralRepository {
        val prefInjection = UserPreference.getInstance(context.dataStore)
        val token = runBlocking { prefInjection.getToken().first() }
        val apiService = ApiConfig.getApiService(token.toString())
        return GeneralRepository.getInstance(apiService, prefInjection)
    }

}