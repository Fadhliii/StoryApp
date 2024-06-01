package com.example.gagalmuluyaallah

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.gagalmuluyaallah.databinding.ActivityUserWelcomeBinding
import com.example.gagalmuluyaallah.model.dataStore
import kotlinx.coroutines.launch

class UserWelcomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserWelcomeBinding
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Inisialisasi LoginViewModel
        loginViewModel = getViewModel(this)

        // Tambahkan listener untuk tombol logout
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
    }
    private fun getViewModel(activity: AppCompatActivity): LoginViewModel {
        val factory = VMFactory.getInstance(
                activity.application,
                UserPreference.getInstance(dataStore)
        )
        return ViewModelProvider(activity, factory)[LoginViewModel::class.java]
    }
}