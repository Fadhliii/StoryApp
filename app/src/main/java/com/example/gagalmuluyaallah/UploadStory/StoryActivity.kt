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

        binding.rvStories.layoutManager = LinearLayoutManager(this@StoryActivity)
        binding.rvStories.adapter = storyAdapter
        storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
        )
        // this one is for detail activity navigation when item clicked in recyclerview list
        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(items: StoryItem?) {
                val intent = Intent(this@StoryActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.ARG_STORY_ITEM, items)
                Log.e("StoryActivity", "Navigating to DetailActivity with StoryItem: ${items?.id}")
                startActivity(intent)
            }
        })
        viewModel.stories.observe(this) {
            if (it != null) {
                when (it) {
                    is ResultSealed.Loading -> {
                        showLoading(true)
                    }
                    is ResultSealed.Success -> {
                        showLoading(false)

                        val response = it.data
                        storyAdapter.submitData(lifecycle, response)
                    }
                    is ResultSealed.Error -> {
                        showLoading(false)
                    }
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

    override fun onResume() {
        super.onResume()
        viewModel.stories
    }

    companion object {
        const val SESSION = "session"
    }
}


