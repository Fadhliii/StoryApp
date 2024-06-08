package com.example.gagalmuluyaallah.UploadStory

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.mycamera.withDateFormat
import com.example.gagalmuluyaallah.R
import com.example.gagalmuluyaallah.databinding.ActivityDetailBinding
import com.example.gagalmuluyaallah.response.StoryItem

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyItem = intent.getParcelableExtra<StoryItem>(ARG_STORY_ITEM)
        if (storyItem != null) {
            setupDetailStory(storyItem)
            Log.e("DetailStoryActivity", "Setting up detail story with StoryItem: ${storyItem.id}")
        } else {
            showToast(getString(R.string.failed_to_load_data))
            Log.e("DetailStoryActivity", "Failed to load data")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@DetailActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupDetailStory(items: StoryItem) {
        binding.apply {
            storyName.text = items.name ?: getString(R.string.not_available)
            storyDate.text = items.createdAt?.withDateFormat() ?: getString(R.string.not_available)
            storyDescription.text = items.description ?: getString(R.string.not_available)
            Glide.with(this@DetailActivity)
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
