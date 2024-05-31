package com.example.gagalmuluyaallah

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.datastore.preferences.preferencesDataStore
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import com.example.gagalmuluyaallah.databinding.ActivityRegisterBinding
import com.example.gagalmuluyaallah.model.Injection

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    //add datastore
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SESSION_TOKEN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)
        setupAction()
    }


    private fun setupAction() {
        binding.buttonregister.setOnClickListener { setupRegister() }
    }

    private fun setupRegister() {
        //set alert dialog
        val registerViewModel = getViewModel(this@RegisterActivity)
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        when {
            name.isEmpty()     -> {
                binding.nameEditText.error = getString(R.string.empty_name)
            }

            email.isEmpty()    -> {
                binding.emailEditText.error = getString(R.string.empty_email)
            }

            password.isEmpty() -> {
                binding.passwordEditText.error = getString(R.string.password_empty)
            }

            else               -> {
                registerViewModel.register(name, email, password).observe(this@RegisterActivity) {
                    if (it != null) {
                        when (it) {
                            is ResultSealed.Loading -> {
                                showLoading(true)
                            }

                            is ResultSealed.Success -> {
                                Log.d("RegisterActivity", "setupRegister: ${it.data}")
                                showLoading(false)
                                Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }

                            is ResultSealed.Error   -> {
                                showLoading(false)
                                ToastMessage()
                            }
                        }
                    }
                }
            }

        }
    }
    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getViewModel(activity: AppCompatActivity): RegisterViewModel {
        val factory = VMFactory.getInstance(
                activity.application,
                UserPreference.getInstance(dataStore)
        )
        return ViewModelProvider(activity, factory).get(RegisterViewModel::class.java) //viewmodelprovider is to get the viewmodel
    }

    private fun ToastMessage() {
        Toast.makeText(this, "Register Failed", Toast.LENGTH_SHORT).show()
    }

    companion object {
        var SESSION_TOKEN = "SESSION_TOKEN"
    }
}
