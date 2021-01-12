package com.ericktijerou.hackernews.presentation.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ericktijerou.hackernews.R
import com.ericktijerou.hackernews.databinding.ItemFeedBinding
import com.ericktijerou.hackernews.domain.entity.News

class FeedPagedListAdapter(
    private val onItemClick: (View, Long) -> Unit
) : PagedListAdapter<News, FeedPagedListAdapter.ViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bindView(it) }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News) =
                oldItem.storyId == newItem.storyId


            override fun areContentsTheSame(oldItem: News, newItem: News) =
                oldItem == newItem

        }
    }

    inner class ViewHolder(private val binding: ItemFeedBinding) : RecyclerView.ViewHolder(binding.clContainer) {
        fun bindView(news: News) {
            binding.news = news
            binding.clContainer.setOnClickListener { onItemClick(it, news.storyId) }
        }
    }
}

