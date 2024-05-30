package com.example.gagalmuluyaallah

import androidx.lifecycle.ViewModel

class RegisterViewModel(val repositoryRVM: GeneralRepository): ViewModel() {
    fun registerGR(name:String, email:String, password:String) = repositoryRVM.registerGR(name, email, password)

}
