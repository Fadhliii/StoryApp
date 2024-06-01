package com.example.gagalmuluyaallah

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gagalmuluyaallah.model.Injection

@Suppress("UNCHECKED_CAST")
class VMFactory(
        private val repository: GeneralRepository,
        private val userPreference: UserPreference,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(UserWelcomeViewModel::class.java) -> {
                return UserWelcomeViewModel(repository, userPreference) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java)    -> {
                return RegisterViewModel(repository) as T //it doesnt need userpref
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java)    -> {
                return LoginViewModel(repository, userPreference) as T
            }
            else                                                     -> throw IllegalArgumentException("Unknown / invalid VM class : " + modelClass.name)
        }
    }


    companion object {
        // volatile is used to make sure that the value of instance is always up-to-date and the same to all execution thread
        @Volatile
        private var instance: VMFactory? = null
        fun getInstance(context: Context, pref: UserPreference): VMFactory =
                instance ?: synchronized(this) {
                    instance ?: VMFactory(Injection.provideRepository(context), pref)
                }.also { instance = it }
    }
}
