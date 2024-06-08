package com.example.gagalmuluyaallah.UploadStory


import StoryAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gagalmuluyaallah.ResultSealed
import com.example.gagalmuluyaallah.connection.GeneralRepository
import com.example.gagalmuluyaallah.connection.UserPreference
import com.example.gagalmuluyaallah.connection.ViewModelFactory
import com.example.gagalmuluyaallah.databinding.ActivityStoryBinding
import com.example.gagalmuluyaallah.model.dataStore
import com.example.gagalmuluyaallah.response.StoryItem
import kotlinx.coroutines.launch

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private val adapter by lazy { StoryAdapter() }
    private var repository = GeneralRepository
    private lateinit var viewModel: StoryViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SESSION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = obtainViewModel(this@StoryActivity)
        setupStory()
    }

    private fun setupStory() {
        val storyAdapter = StoryAdapter()
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = storyAdapter
        binding.rvStories.setHasFixedSize(true)

        // Set up the click listener for each story item.
        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(items: StoryItem?) {
                val intent = Intent(this@StoryActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.ARG_STORY_ITEM, items)
                Log.e("StoryActivity", "Navigating to DetailActivity with StoryItem: ${items?.id}")
                startActivity(intent)
            }
        })

        // Observe the stories LiveData from the ViewModel.
        viewModel.stories.observe(this) {
            when (it) {
                is ResultSealed.Loading -> {
                    showLoading(true)
                }
                is ResultSealed.Success -> {
                    showLoading(false)
                    val response = it.data
                    storyAdapter.setData(response)
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }
                is ResultSealed.Error -> {
                    showLoading(false)
                    Log.e("StoryActivity Error", "Error: ${it.exception}")
                }
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): StoryViewModel {
        val factory = ViewModelFactory.getInstance(
                activity.application,
                UserPreference.getInstance(activity.dataStore)
        )
        return ViewModelProvider(activity, factory)[StoryViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val SESSION = "session"
    }
}




