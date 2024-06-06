package com.example.gagalmuluyaallah.View

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.gagalmuluyaallah.UploadStory.StoryActivity
import com.example.gagalmuluyaallah.connection.UserPreference
import com.example.gagalmuluyaallah.connection.ViewModelFactory
import com.example.gagalmuluyaallah.databinding.ActivityUserWelcomeBinding
import com.example.gagalmuluyaallah.model.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserWelcomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserWelcomeBinding
    private lateinit var loginViewModel: LoginViewModel
    private val userPreference: UserPreference by lazy {
        UserPreference.getInstance(dataStore)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Inisialisasi LoginViewModel
        loginViewModel = getViewModel(this)
        // get email user
        lifecycleScope.launch {
            val userEmail = userPreference.getUserEmail().first() // Ambil email pengguna
            binding.emailText.text = userEmail // Tampilkan email pengguna
        }
        // Logout
        binding.logout.setOnClickListener {
            // Panggil fungsi logout di LoginViewModel
            lifecycleScope.launch {
                // Panggil fungsi logout di LoginViewModel
                loginViewModel.logout()
            }

            // Arahkan pengguna kembali ke LoginActivity
            val intent = Intent(this@UserWelcomeActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // upload story
        binding.btnUploadStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding.btnCheckAllStories.setOnClickListener {
            val intent = Intent(this, StoryActivity ::class.java)
            startActivity(intent)
        }

    }

    // Fungsi untuk mendapatkan instance LoginViewModel
    private fun getViewModel(activity: AppCompatActivity): LoginViewModel {
        val factory = ViewModelFactory.getInstance(
                activity.application,
                UserPreference.getInstance(dataStore)
        )
        return ViewModelProvider(activity, factory)[LoginViewModel::class.java]
    }
}