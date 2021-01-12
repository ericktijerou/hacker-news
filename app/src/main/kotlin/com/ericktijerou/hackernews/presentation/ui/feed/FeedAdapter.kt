package com.ericktijerou.hackernews.presentation.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ericktijerou.hackernews.R
import com.ericktijerou.hackernews.databinding.ItemFeedBinding
import com.ericktijerou.hackernews.domain.entity.News

class FeedAdapter(
    private val onItemClick: (View, Long) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val newsList = mutableListOf<News>()

    fun updateNewsList(newsList: List<News>) {
        with(this.newsList) {
            clear()
            addAll(newsList)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FeedViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_feed, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val news = newsList[position]
        val binding = (holder as FeedViewHolder).binding
        binding.news = news
        binding.clContainer.setOnClickListener { onItemClick(it, news.storyId) }
    }

    override fun getItemCount() = newsList.size

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        (holder as FeedViewHolder).binding.clContainer.setOnClickListener(null)
    }

}

private class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding: ItemFeedBinding = ItemFeedBinding.bind(view)

}