package com.example.gagalmuluyaallah.connection

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gagalmuluyaallah.Map.MapsViewModel
import com.example.gagalmuluyaallah.UploadStory.StoryViewModel
import com.example.gagalmuluyaallah.View.AddStoryViewModel
import com.example.gagalmuluyaallah.View.LoginViewModel
import com.example.gagalmuluyaallah.View.RegisterViewModel
import com.example.gagalmuluyaallah.model.Injection
import com.example.gagalmuluyaallah.View.UserWelcomeViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
        private val repository: GeneralRepository,
        private val pref: UserPreference,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(UserWelcomeViewModel::class.java) -> {
                return UserWelcomeViewModel(repository, pref) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java)    -> {
                return RegisterViewModel(repository) as T //it doesnt need userpref
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java)       -> {
                return LoginViewModel(repository, pref) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                return AddStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                return StoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java)  -> {
                return MapsViewModel(repository) as T
            }
            else                                                    -> throw IllegalArgumentException("Unknown / invalid VM class : " + modelClass.name)
        }
    }


    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, pref: UserPreference): ViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: ViewModelFactory(Injection.provideRepository(context), pref)
                }.also { instance = it }
    }
}
