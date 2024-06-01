package com.example.gagalmuluyaallah

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gagalmuluyaallah.databinding.ActivityLoginBinding
import com.example.gagalmuluyaallah.model.Injection.dataStore

class LoginActivity : AppCompatActivity() {
    private lateinit var userPreference: UserPreference
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Loading(true)
        userPreference = UserPreference.getInstance(dataStore)
        // check if user already login

    }
    private fun Loading(isLoading : Boolean) {
        this.binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
        val loading2 = this.binding.progressBar2.isIndeterminate
        binding.progressBar2.isIndeterminate = loading2
    }
}