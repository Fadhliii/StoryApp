package com.example.gagalmuluyaallah.View

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.mycamera.getImageUri
import com.dicoding.picodiploma.mycamera.uriToFile
import com.example.gagalmuluyaallah.R
import com.example.gagalmuluyaallah.ResultSealed
import com.example.gagalmuluyaallah.connection.UserPreference
import com.example.gagalmuluyaallah.connection.ViewModelFactory
import com.example.gagalmuluyaallah.databinding.ActivityAddStoryBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SESSION)

    private lateinit var storyViewModel: AddStoryViewModel
    private fun allPermissionsGranted() =
            ContextCompat.checkSelfPermission(
                    this,
                    REQUIRED_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
    private var currentImageUri: Uri? = null // Image URI from gallery
    private var getFile: File? = null
    private val requestPermissionLauncher =
            registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                val allPermissionsGranted = permissions.entries.all { it.value }
                if (allPermissionsGranted) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
    private fun checkPermission(permission: String): Boolean { // Check if the permission has been granted for camera
        return ContextCompat.checkSelfPermission(
                this,
                permission
        ) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storyViewModel = getViewModel(this@AddStoryActivity)
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(arrayOf(REQUIRED_PERMISSION))
        }
        showLoading(false) // Hide loading bar
        setupAction()
        binding.btnUpload.setOnClickListener { uploadStory() }
    }
    private fun setupAction() {
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
//        binding.uploadButton.setOnClickListener { uploadStory() }
    }
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private val launcherGallery = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri

            showImage()
        } else {
            Log.d("Photo Picker", getString(R.string.no_media_selected))
        }
    }
    private val launcherIntentGallery = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this)

            getFile = myFile
            Log.d("Image File", "showImage: ${myFile?.path}")
        }
    }
    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imageView2.setImageURI(it)
            Toast.makeText(this, "Show image: $it", Toast.LENGTH_SHORT).show()
            Snackbar.make(binding.root, "Image URI: $it", Snackbar.LENGTH_SHORT).show()
        }
    }
    private val launcherIntentCamera = registerForActivityResult(
            ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun getViewModel(activity: AppCompatActivity): AddStoryViewModel {
        val factory = ViewModelFactory.getInstance(
                activity.application,
                UserPreference.getInstance(dataStore)
        )
        return ViewModelProvider(activity, factory)[AddStoryViewModel::class.java]
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun toUSerWelcome() {
        val intent = Intent(this, UserWelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun uploadStory() {
        if (currentImageUri == null) {
          Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this)
            val descriptionBody = binding.textinput.toString()

            // Langsung panggil fungsi uploadStoryWithoutLocation
            uploadStoryWithoutLocation(imageFile, descriptionBody, null, null)
        }
    }

    private fun uploadStoryWithoutLocation(imageFile: File, description: String, lat: Double?, lon: Double?) {
        storyViewModel.AddNewStory(imageFile, description, lat, lon).observe(this) {
            if (it != null) {
                when (it) {
                    is ResultSealed.Loading -> {
                        showLoading(true)

                    }
                    is ResultSealed.Success -> {
                        showLoading(false)
                        val response = it.data
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        Log.d("AddStoryActivity", "uploadStoryWithoutLocation: ${response.message}")
                        finish()
                    }
                    is ResultSealed.Error -> {
                        showLoading(false)
                        Toast.makeText(this, it.exception, Toast.LENGTH_SHORT).show()
                        Log.e("AddStoryActivity", "uploadStoryWithoutLocation: ${it.exception}")
                    }
                }
            }
        }
    }

    companion object {

        const val SESSION = "session"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA

    }
}