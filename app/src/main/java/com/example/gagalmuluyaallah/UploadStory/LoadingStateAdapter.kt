package com.example.gagalmuluyaallah.UploadStory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gagalmuluyaallah.databinding.ItemLoadingBinding

class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
            LoadingStateViewHolder(ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false), retry)

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) = holder.bind(loadState)

    class LoadingStateViewHolder(private val binding: ItemLoadingBinding, retry: () -> Unit) :
            RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryButton.setOnClickListener { retry() }
        }
        fun bind(loadState: LoadState) {
            binding.apply {
                errorMessage.text = if (loadState is LoadState.Error) loadState.error.localizedMessage else ""
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMessage.isVisible = loadState is LoadState.Error
            }
        }
    }
}