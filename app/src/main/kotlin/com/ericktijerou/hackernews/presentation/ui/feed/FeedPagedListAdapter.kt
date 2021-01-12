package com.ericktijerou.hackernews.presentation.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ericktijerou.hackernews.R
import com.ericktijerou.hackernews.core.LoadingState
import com.ericktijerou.hackernews.core.orZero
import com.ericktijerou.hackernews.databinding.ItemFeedBinding
import com.ericktijerou.hackernews.domain.entity.News
import com.ericktijerou.hackernews.presentation.ui.util.getRelativeTime
import com.ericktijerou.hackernews.presentation.ui.util.toSpanned
import koleton.api.generateSkeleton
import koleton.custom.KoletonView

class FeedPagedListAdapter(
    private val onItemClick: (View, Long) -> Unit
) : PagedListAdapter<News, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
) {
    private var state = LoadingState.NONE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_VIEW -> (holder as FeedViewHolder).bindView(getItem(position))
            TYPE_SKELETON -> (holder as SkeletonViewHolder).koletonView.showSkeleton()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view = ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when (viewType) {
            TYPE_VIEW -> FeedViewHolder(view)
            TYPE_SKELETON -> SkeletonViewHolder(view.clContainer.generateSkeleton())
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            TYPE_SKELETON
        } else {
            TYPE_VIEW
        }
    }

    private fun hasExtraRow() = state != LoadingState.NONE && state != LoadingState.LOADED

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setState(@LoadingState newState: Int) {
        val previousState = this.state
        val hadExtraRow = hasExtraRow()
        this.state = newState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    inner class FeedViewHolder(private val binding: ItemFeedBinding) : RecyclerView.ViewHolder(binding.clContainer) {
        fun bindView(news: News?) {
            news?.apply {
                binding.clContainer.setOnClickListener { onItemClick(it, storyId.orZero()) }
                binding.tvTitle.text = title.toSpanned()
                binding.tvAuthor.text = author
                binding.tvTime.text = date.getRelativeTime()
            }
        }
    }

    class SkeletonViewHolder(val koletonView: KoletonView) : RecyclerView.ViewHolder(koletonView)

    companion object {
        private const val TYPE_VIEW = 1
        private const val TYPE_SKELETON = 2

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News) =
                oldItem.storyId == newItem.storyId


            override fun areContentsTheSame(oldItem: News, newItem: News) =
                oldItem == newItem
        }
    }
}

