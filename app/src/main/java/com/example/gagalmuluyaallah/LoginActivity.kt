package com.example.gagalmuluyaallah

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.gagalmuluyaallah.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class LoginActivity : AppCompatActivity() {
    private lateinit var userPreference: UserPreference
    private lateinit var binding: ActivityLoginBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SESSION)
    private fun succesLogin() {
        startActivity(Intent(this, UserWelcomeActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingBar(false)
        userPreference = UserPreference.getInstance(dataStore)
        // check if user already login
        lifecycleScope.launch {
            try {
                userPreference.isLoggedIn().collect { isLoggedIn ->
                    if (isLoggedIn == true) { // if user already login then go to welcome activity
                        succesLogin()
                    } else {
                        actionLogin()
                        loadingBar(false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace() // if error happen
                Log.e("LoginActivity", "Error: ${e.message}")

            } catch (_: CancellationException) {
                Log.e("LoginActivity", "Error: CancellationException")
            }
        }
    }

    // action login button clicked by user
    private fun actionLogin() {
        binding.btnlogin.setOnClickListener {

            setupLogin()

//            val email = binding.InputEmail.toString()
//            val password = binding.InputPassword.text.toString()
//            if (email.isEmpty() || password.isEmpty()) {
//                binding.etEmail.error = "Email or Password is empty"
//                binding.etPassword.error = "Email or Password is empty"
//                return@setOnClickListener
//            }
//            lifecycleScope.launch {
//                try {
//                    loadingBar(true)
//                    val token = userPreference.getToken().first()
//                    if (token.isNullOrEmpty()) {
//                        val response = userPreference.saveToken(SESSION_TOKEN)
//                        Log.d("LoginActivity", "Token: $response")
//                    }
//                    userPreference.userLogin(true)
//                    succesLogin()
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    Log.e("LoginActivity", "Error: ${e.message}")
//                } finally {
//                    loadingBar(false)
//                }
            }
        }

    private fun setupLogin() {
        val loginViewModel = getViewModel(this@LoginActivity)
        val email = binding.InputEmail.text.toString() //email
        val password = binding.InputPassword.text.toString() // password

        when {
            email.isEmpty()    -> {
                binding.InputEmail.error = getString(R.string.empty_email)
            }

            password.isEmpty() -> {
                binding.InputPassword.error = getString(R.string.password_empty)
            }

            else               -> {loginViewModel.loginViewModel(email, password).observe(this@LoginActivity){
                if(it != null){
                    when(it){
                        is ResultSealed.Loading->{
                            loadingBar(true)
                        }
                        is ResultSealed.Success->{
                            loadingBar(false)
                            val response = it.data
                            loginViewModel.saveLoginViewModel(response.token.toString())
                            showToast(getString(R.string.Login_succes))
                            val intent = Intent(this@LoginActivity, UserWelcomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        is ResultSealed.Error -> {
                            loadingBar(false)
                            Log.e("Login error", "Login user error : ${it.exception}")
                            // Membuat AlertDialog
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle("Login Error")
                                setMessage("Email atau password yang Anda masukkan salah. Silakan coba lagi.")
                                setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }
                            }.show()
                        }
                    }
                }
            }

            }

        }
    }
    private fun getViewModel(activity: AppCompatActivity): LoginViewModel {
        val factory = VMFactory.getInstance(
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
