package com.example.gagalmuluyaallah.View

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.gagalmuluyaallah.R
import com.example.gagalmuluyaallah.connection.UserPreference
import com.example.gagalmuluyaallah.databinding.ActivityMainBinding
import com.example.gagalmuluyaallah.model.dataStore
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var userPreference: UserPreference
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, statusBarInsets.top, 0, 0)
            insets
        }

        //set background button\
        binding.button.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.buttonlogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        userPreference = UserPreference.getInstance(dataStore)
        lifecycleScope.launch {
            try {
                userPreference.isLoggedIn().collect { isLoggedIn ->
                    if (isLoggedIn == true) { // if user already login then go to welcome activity
                        succesLogin()
                    } else {

                    }
                }
            } catch (_: CancellationException) {
                Log.e("LoginActivity", "Error: CancellationException")
            }
        }



        // Menyembunyikan tombol navigasi
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        setupAnimation()
        setAppName()
        //set background button

    }

    private fun succesLogin() {
        startActivity(Intent(this, UserWelcomeActivity::class.java))
        finish()
    }

    fun setAppName() {
        val appName = getString(R.string.app_name)
        //set color val appname
        val welcomeMessage = getString(R.string.welcome_message) + " " + appName
        //set spanable
        val spannable = SpannableString(welcomeMessage)
        //set color
        val backgroundColorSpan = BackgroundColorSpan(ContextCompat.getColor(this, R.color.white))
        val foregroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.black))

        val start = welcomeMessage.indexOf(appName) // get index of appname
        val end = start + appName.length // get end of appname

        spannable.setSpan(backgroundColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(foregroundColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Membuat animasi berkedip
        val animator = ObjectAnimator.ofFloat(binding.textView, "alpha", -100f, 400f).apply {
            duration = 100 // durasi dalam milidetik
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        animator.start()
        binding.textView.text = spannable
    }

    fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.button, View.TRANSLATION_Y, -15f, 15f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val button = ObjectAnimator.ofFloat(binding.button, View.TRANSLATION_Y, -15f, 15f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}

