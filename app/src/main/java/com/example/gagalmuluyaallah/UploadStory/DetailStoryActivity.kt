package com.example.gagalmuluyaallah.UploadStory

import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.mycamera.withDateFormat
import com.example.gagalmuluyaallah.R
import com.example.gagalmuluyaallah.databinding.ActivityDetailStoryBinding
import com.example.gagalmuluyaallah.response.StoriesItemsResponse

@Suppress("DEPRECATION")
class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)

        val storiesItemsResponse = intent.getParcelableExtra<StoriesItemsResponse>(ARG_STORY_ITEM)
        if (storiesItemsResponse != null) {
            setupDetailStory(storiesItemsResponse)
            Log.e("DetailStoryActivity", "Setting up detail story with StoryItem != : ${storiesItemsResponse.id}")
        } else {
            showToast(getString(R.string.failed_to_load_data))
            Log.e("DetailStoryActivity", "Failed to load data")
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this@DetailStoryActivity, message, Toast.LENGTH_SHORT).show()
    }
    private fun setupDetailStory(items: StoriesItemsResponse) {
        binding.apply {
            storyName.text = items.name ?: getString(R.string.not_available)
            storyDate.text = items.createdAt?.withDateFormat() ?: getString(R.string.not_available)
            storyDescription.text = items.description ?: getString(R.string.not_available)
            Glide.with(this@DetailStoryActivity)
                .load(items.photoUrl)
                .centerCrop()
                .into(storyImage)
            Log.e("DetailStoryActivity", "Setting up detail story with StoryItem: ${items.id}")
        }
    }
    companion object {
        const val ARG_STORY_ITEM = "arg_story_item"
    }
}