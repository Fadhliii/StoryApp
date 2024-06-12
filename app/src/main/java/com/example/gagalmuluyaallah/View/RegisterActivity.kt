package com.example.gagalmuluyaallah.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.datastore.preferences.preferencesDataStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasiku.customView.MyEmailText
import com.example.aplikasiku.customView.MyNameText
import com.example.aplikasiku.customView.MyPasswordText
import com.example.gagalmuluyaallah.R
import com.example.gagalmuluyaallah.ResultSealed
import com.example.gagalmuluyaallah.connection.UserPreference
import com.example.gagalmuluyaallah.connection.ViewModelFactory
import com.example.gagalmuluyaallah.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var emailEditText: MyEmailText
    private lateinit var passwordEditText: MyPasswordText
    private lateinit var nameEditText: MyNameText

    //add datastore
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SESSION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        nameEditText = findViewById(R.id.nameEditText)

        showLoading(false)
        setupAction()
    }

    private fun setupAction() {
        binding.buttonregister.setOnClickListener { setupRegister() }
    }

    private fun setupRegister() {
        val registerViewModel = getViewModel(this@RegisterActivity)
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        when {
            binding.nameEditText.error != null ->{
                binding.nameEditText.error = getString(R.string.empty_name)
            }
            binding.emailEditText.error != null -> {
                binding.emailEditText.error = getString(R.string.email_invalid)
            }
            binding.passwordEditText.error != null -> {
                binding.passwordEditText.error = getString(R.string.password_empty)
            }
            else -> {
                registerViewModel.register(name, email, password).observe(this@RegisterActivity) {
                    if (it != null) {
                        when (it) {
                            is ResultSealed.Loading -> {
                                showLoading(true)
                            }
                            is ResultSealed.Success -> {
                                Log.d("RegisterActivity", "setupRegister: ${it.data}")
                                showLoading(false)
                                Toast.makeText(this, "Register Success", Toast.LENGTH_LONG).show()
                                val snackbar = Snackbar.make(binding.root, "Register Success", Snackbar.LENGTH_LONG)
                                snackbar.setBackgroundTint(ContextCompat.getColor(this@RegisterActivity, R.color.green))
                                snackbar.show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            is ResultSealed.Error -> {
                                showLoading(false)
                                ToastMessage()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getViewModel(activity: AppCompatActivity): RegisterViewModel {
        val factory = ViewModelFactory.getInstance(
                activity.application,
                UserPreference.getInstance(dataStore)
        )
        return ViewModelProvider(activity, factory).get(RegisterViewModel::class.java)
    }

    private fun ToastMessage() {
        Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val SESSION = "session"
    }
}
