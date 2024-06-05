@file:Suppress("DEPRECATION")

package com.example.gagalmuluyaallah.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.gagalmuluyaallah.connection.GeneralRepository
import com.example.gagalmuluyaallah.connection.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")
object Injection {
    fun provideRepository(context: Context): GeneralRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val token = runBlocking { pref.getToken().first() }
        val apiService = ApiConfig.getApiService(token.toString())
        return GeneralRepository.getInstance(apiService, pref)
    }
}