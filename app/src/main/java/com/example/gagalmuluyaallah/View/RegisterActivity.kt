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
import com.example.gagalmuluyaallah.R
import com.example.gagalmuluyaallah.ResultSealed
import com.example.gagalmuluyaallah.connection.UserPreference
import com.example.gagalmuluyaallah.connection.ViewModelFactory
import com.example.gagalmuluyaallah.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar

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
        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length <= 6) {
                    binding.nameEditText.error = getString(R.string.empty_name)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                BackgroundColorSpan(ContextCompat.getColor(this@RegisterActivity, R.color.green))

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                BackgroundColorSpan(ContextCompat.getColor(this@RegisterActivity, R.color.green))
            }

        })

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isValidEmail(s.toString())) {
                    binding.emailEditText.error = getString(R.string.email_invalid)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                BackgroundColorSpan(ContextCompat.getColor(this@RegisterActivity, R.color.green))

            }
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length <= 6) {
                    binding.passwordEditText.error = "Password harus lebih dari 6 karakter"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        binding.buttonregister.setOnClickListener { setupRegister() }
    }

    // this one is to send the registeer data from user to API database and save it
    private fun setupRegister() {
        //set alert dialog
        val registerViewModel = getViewModel(this@RegisterActivity)
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        when {
            name.length <= 6     -> {
                binding.nameEditText.error = getString(R.string.empty_name)
            }

            !isValidEmail(email) -> binding.emailEditText.error = getString(R.string.email_invalid)
            password.isEmpty()   -> {
                binding.passwordEditText.error = getString(R.string.password_empty)
            }

            password.length <= 6 -> {
                binding.passwordEditText.error = getString(R.string.password_empty)
            }

            else                 -> {
                registerViewModel.register(name, email, password).observe(this@RegisterActivity) {
                    if (it != null) { //it is the result from the registerViewModel
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
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() //format email validation
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getViewModel(activity: AppCompatActivity): RegisterViewModel {
        val factory = ViewModelFactory.getInstance(
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
