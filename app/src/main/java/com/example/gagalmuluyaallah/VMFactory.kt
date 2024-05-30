package com.example.gagalmuluyaallah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class VMFactory (
        private val repository: GeneralRepository,
        private val userPreference: UserPreference
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelFactory: Class<T>):T {
        when{
            modelFactory.isAssignableFrom(MainVM::class.java) -> {
                return MainVM(repository, userPreference) as T
            }
            modelFactory.isAssignableFrom(MainVM::class.java) -> {
                return RegisterViewModel(repository, userPreference) as T}

            else -> throw IllegalArgumentException("Unkown / invalid VM class : " + modelFactory.name)
        }
    }
}