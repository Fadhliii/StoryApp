package com.example.gagalmuluyaallah.connection

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.mycamera.reduceFileImage
import com.example.gagalmuluyaallah.ResultSealed
import com.example.gagalmuluyaallah.local.StoryDatabase
import com.example.gagalmuluyaallah.local.StoryRemoteMediator
import com.example.gagalmuluyaallah.model.ApiConfig
import com.example.gagalmuluyaallah.model.ApiService
import com.example.gagalmuluyaallah.response.GeneralResponse
import com.example.gagalmuluyaallah.response.LoginResponse
import com.example.gagalmuluyaallah.response.LoginResult
import com.example.gagalmuluyaallah.response.StoriesResponse
import com.example.gagalmuluyaallah.response.StoriesItemsResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class GeneralRepository private constructor(
        private var apiService: ApiService,
        private val userPreference: UserPreference,
        private val storyDatabase: StoryDatabase,
) {
    private var token: String? = null

    private suspend fun getToken(): String? = token ?: runBlocking {
        userPreference.getToken().first()
    }.also { token = it }

    fun register(name: String, email: String, password: String): LiveData<ResultSealed<GeneralResponse>> = liveData {
        emit(ResultSealed.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(ResultSealed.Success(response))
        } catch (e: Exception) {
            emit(ResultSealed.Error(e.message.toString()))
        }
    }

    fun login(email: String, password: String): LiveData<ResultSealed<LoginResult>> = liveData {
        emit(ResultSealed.Loading)
        try {
            val response = apiService.login(email, password)
            emit(ResultSealed.Success(response.loginResult!!))
        } catch (e: Exception) {
            emit(ResultSealed.Error(e.message.toString()))
        }
    }

    fun uploadNewStory(file: File?, description: String, lat: Double?, lon: Double?): LiveData<ResultSealed<GeneralResponse>> = liveData {
        emit(ResultSealed.Loading)
        try {
            val imageFile = reduceFileImage(file!!)
            val descriptionBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData("photo", imageFile.name, requestImageFile)
            val response = apiService.addNewStory(multipartBody, descriptionBody, lat, lon)
            emit((ResultSealed.Success(response)))
        } catch (e: Exception) {
            emit(ResultSealed.Error(e.message.toString()))
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getAllStories(coroutineScope: CoroutineScope): LiveData<ResultSealed<PagingData<StoriesItemsResponse>>> = liveData {
        emit(ResultSealed.Loading)
        try {
            val token = getToken()
            apiService = ApiConfig.getApiService(token.toString())
            val pager = Pager(
                    config = PagingConfig(pageSize = 5),
                    remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
                    pagingSourceFactory = { storyDatabase.storyDao().getAllStory() }
            )
            val pagingDataFlow = pager.flow.cachedIn(coroutineScope)
            pagingDataFlow.collect { emit(ResultSealed.Success(it)) }
        } catch (e: Exception) {
            emit(ResultSealed.Error(e.message.toString()))
        }
    }

    fun getStoriesWithLocation(): LiveData<ResultSealed<List<StoriesItemsResponse>>> = liveData {
        emit(ResultSealed.Loading)
        try {
            val token = getToken()
            apiService = ApiConfig.getApiService((token.toString()))
            val response = apiService.getStoriesWithLocation()
            emit(ResultSealed.Success(response.listStory))
        } catch (e: Exception) {
            emit(ResultSealed.Error(e.message.toString()))
        }
    }

    companion object {
        private var instance: GeneralRepository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference, storyDatabase: StoryDatabase): GeneralRepository {
            return instance ?: synchronized(this) {
                instance ?: GeneralRepository(apiService, userPreference, storyDatabase).also { instance = it }
            }
        }
    }
}