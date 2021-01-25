package com.ericktijerou.hackernews.presentation.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ericktijerou.hackernews.core.Status
import com.ericktijerou.hackernews.core.gone
import com.ericktijerou.hackernews.core.visible
import com.ericktijerou.hackernews.databinding.ItemFeedBinding
import com.ericktijerou.hackernews.databinding.ItemNetworkStateBinding
import com.ericktijerou.hackernews.domain.entity.News
import com.ericktijerou.hackernews.presentation.ui.util.getRelativeTime
import com.ericktijerou.hackernews.presentation.ui.util.toSpanned

class FeedPagedListAdapter(
    private val onItemClick: (String) -> Unit,
    private val onFavoriteItemClick: (String, Boolean) -> Unit
) : PagedListAdapter<News, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
) {
    private var state = Status.NONE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_VIEW -> (holder as FeedViewHolder).bindView(getItem(position))
            TYPE_NETWORK -> (holder as NetworkStateViewHolder).showProgress()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_VIEW -> {
                val view =
                    ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FeedViewHolder(view)
            }
            TYPE_NETWORK -> {
                val view = ItemNetworkStateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                NetworkStateViewHolder(view)
            }
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            TYPE_NETWORK
        } else {
            TYPE_VIEW
        }
    }

    private fun hasExtraRow() =
        state != Status.NONE && state != Status.LOADED && state != Status.FAILED

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setState(@Status newState: Int) {
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

    inner class FeedViewHolder(private val binding: ItemFeedBinding) :
        CustomViewHolder(binding.clItemContainer) {

        val viewForeground = binding.viewForeground

        fun bindView(news: News?) {
            news?.apply {
                binding.clItemContainer.setOnClickListener { onItemClick(url) }
                binding.tvTitle.text = title.toSpanned()
                binding.tvAuthor.text = author
                binding.tvTime.text = date.getRelativeTime()
                binding.tbFavorite.isChecked = isFavorite
                binding.tbFavorite.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (buttonView.isPressed) {
                        buttonView.startAnimation(buildAnimation())
                        onFavoriteItemClick(id, isChecked)
                    }
                }
            }
        }

        private fun buildAnimation(): ScaleAnimation {
            val scaleAnimation = ScaleAnimation(
                0.7f,
                1.0f,
                0.7f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            scaleAnimation.duration = 500
            scaleAnimation.interpolator = BounceInterpolator()
            return scaleAnimation
        }

        override fun recycle() {
            binding.clItemContainer.setOnClickListener(null)
            binding.tvTitle.text = null
            binding.tvAuthor.text = null
            binding.tvTime.text = null
        }
    }

    class NetworkStateViewHolder(private val binding: ItemNetworkStateBinding) :
        CustomViewHolder(binding.clProgressBar) {

        fun showProgress() {
            binding.lottieView.playAnimation()
            binding.lottieView.visible()
        }

        override fun recycle() {
            binding.lottieView.cancelAnimation()
            binding.lottieView.gone()
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        (holder as CustomViewHolder).recycle()
    }

    fun getItemIdByPosition(position: Int): String {
        return getItem(position)?.id.orEmpty()
    }

    companion object {
        private const val TYPE_VIEW = 1
        private const val TYPE_NETWORK = 2

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News) =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: News, newItem: News) =
                oldItem == newItem
        }
    }
}

