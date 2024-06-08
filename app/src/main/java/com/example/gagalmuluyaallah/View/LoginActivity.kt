package com.example.gagalmuluyaallah.View

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.gagalmuluyaallah.R
import com.example.gagalmuluyaallah.ResultSealed
import com.example.gagalmuluyaallah.connection.UserPreference
import com.example.gagalmuluyaallah.connection.ViewModelFactory
import com.example.gagalmuluyaallah.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class LoginActivity : AppCompatActivity() {
    private lateinit var userPreference: UserPreference
    private lateinit var binding: ActivityLoginBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SESSION)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingBar(false)

        userPreference = UserPreference.getInstance(dataStore)
        lifecycleScope.launch {
            try {
                userPreference.isLoggedIn().collect { isLoggedIn ->
                    if (isLoggedIn == true) {
                        startActivity(Intent(this@LoginActivity, UserWelcomeActivity::class.java))
                        finish()
                    } else {
                        setupAction()
                    }
                }
            } catch (_: CancellationException) {
            } catch (e: Exception) {
                Log.e("LoginActivity", "Error in collecting user login status", e)
            }
        }
    }

    // action login button clicked by user
    private fun setupAction() {
        binding.btnlogin.setOnClickListener { setupLogin() }

        // Adding text change listeners for input fields
        binding.InputEmail.addTextChangedListener(loginTextWatcher)
        binding.InputPassword.addTextChangedListener(loginTextWatcher)
    }

    private val loginTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            validateLoginForm()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun validateLoginForm() {
        val email = binding.InputEmail.text.toString().trim()
        val password = binding.InputPassword.text.toString().trim()
        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length > 6

        if (isEmailValid && isPasswordValid) {
            binding.btnlogin.setBackgroundColor(ContextCompat.getColor(this, R.color.secondary_green))
            binding.btnlogin.isEnabled = true
        } else {
            binding.btnlogin.setBackgroundColor(ContextCompat.getColor(this, R.color.secondary_red))
            binding.btnlogin.isEnabled = false
        }
    }

    private fun succesLogin() {
        startActivity(Intent(this, UserWelcomeActivity::class.java))
        finish() // Menambahkan in
    }

    private fun setupLogin() {
        val loginViewModel = getViewModel(this@LoginActivity)

        val email = binding.InputEmail.text.toString()
        val password = binding.InputPassword.text.toString()

        when {
            email.isEmpty() -> {
                binding.InputEmail.error = getString(R.string.empty_email)
            }
            password.isEmpty() -> {
                binding.InputPassword.error = getString(R.string.empty_password)
            }
            else -> {
                loginViewModel.login(email, password).observe(this@LoginActivity) {
                    if (it != null) {
                        when (it) {
                            is ResultSealed.Loading -> {
                                loadingBar(true)
                            }
                            is ResultSealed.Success -> {
                                loadingBar(false)

                                val response = it.data

                                loginViewModel.saveLoginState(response.token.toString())
                                showToast(getString(R.string.successfully_logged_in))

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            is ResultSealed.Error -> {
                                loadingBar(false)
                                Log.e("LoginActivity", "Error: ${it.exception}")
                                AlertDialog.Builder(this@LoginActivity)
                                    .setTitle("Login Failed")
                                    .setMessage("Failed to login. Please check your email and password.")
                                    .setPositiveButton("OK") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .show()

                            }
                        }
                    }
                }
            }
        }
    }
    private fun getViewModel(activity: AppCompatActivity): LoginViewModel {
        val factory = ViewModelFactory.getInstance(
                activity.application,
                UserPreference.getInstance(dataStore)
        )
        return ViewModelProvider(activity, factory)[LoginViewModel::class.java]
    }


    fun loadingBar(isLoading: Boolean) {
        binding.progressBar1.visibility = if (isLoading) View.VISIBLE else View.GONE

    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val SESSION = "session"
    }
}
