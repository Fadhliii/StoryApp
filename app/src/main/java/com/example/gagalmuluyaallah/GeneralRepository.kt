package com.example.gagalmuluyaallah

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class GeneralRepository private constructor(
        private var apiService: ApiService,
        private val userPreference: UserPreference,
) {
    // set token variable
    private var token: String? = null

    // get token from user preference
    //the method is called suspend because its need to wait until the token is ready
    // so it can run in the background
    suspend fun getToken(): String? = token ?: runBlocking {
        // runblocking is block the thread until the token is ready
        userPreference.getToken().first()
    }.also {
        token = it // set token
    }

    // this function is to send the register user to dicoding API Database.
    //@PARAM name
    //@PARAM email
    //@Param password
    fun register(name: String, email: String, password: String): LiveData<ResultSealed<GeneralResponse>> = liveData {
        emit(ResultSealed.Loading)
        Log.d("GeneralRepository", "register: $name, $email, $password")
        try {
            val response = apiService.doRegister(name, email, password)
            if (!response.error!!) {
                emit(ResultSealed.Success(response))
                Log.d("GeneralRepository", "register sukses: ${response.message}")
            }
            else {
                emit(ResultSealed.Error(response.message.toString()))
                Log.e("GeneralRepository error 1 :", "register 01 : ${response.message}")
            }
            emit(ResultSealed.Success(response))
        } catch (e: HttpException) {
            // this one is for the error response from the server <<<<< (●'◡'●)
            val errorBody = e.response()?.errorBody()?.string()
            val response = Gson().fromJson(errorBody, GeneralResponse::class.java)
            emit(ResultSealed.Error(response.message.toString()))
            Log.e("GeneralRepository error 2 :", "register 02 : ${response.message}")
        } catch (e: Exception) {
            // and this one is for the error response from the app <<<<<<< ☜(ﾟヮﾟ☜)
            emit(ResultSealed.Error(e.message.toString()))
            Log.e("GeneralRepository error 3", "register 03: ${e.message}")
        }
    }

    companion object {
        //set instance to GeneralRepository
        private var instance: GeneralRepository ?= null

        // this one is to set the instance so it can be use somewhere else
        fun getInstance( // instance for api and userpreference to save the token
                apiService: ApiService, //from ApiService
                userPreference: UserPreference,
        ): GeneralRepository
        {
            return instance ?: synchronized(this) {
                instance ?: GeneralRepository(apiService, userPreference).also { instance = it }
            }
        }
    }
}