package com.ericktijerou.hackernews.presentation.ui.feed

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ericktijerou.hackernews.R
import com.ericktijerou.hackernews.core.Status
import com.ericktijerou.hackernews.core.gone
import com.ericktijerou.hackernews.core.visible
import com.ericktijerou.hackernews.databinding.ActivityFeedBinding
import com.ericktijerou.hackernews.domain.entity.News
import com.ericktijerou.hackernews.presentation.ui.BaseActivity
import com.ericktijerou.hackernews.presentation.ui.detail.WebViewActivity
import com.ericktijerou.hackernews.presentation.ui.util.observe
import com.ericktijerou.hackernews.presentation.ui.util.startNewActivity
import com.ericktijerou.hackernews.presentation.ui.util.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedActivity : BaseActivity<ActivityFeedBinding>() {

    override fun getViewBinding(): ActivityFeedBinding = ActivityFeedBinding.inflate(layoutInflater)

    private val viewModel by viewModel<FeedViewModel>()

    private val feedAdapter by lazy {
        FeedPagedListAdapter(::goToDetail, ::onFavoriteClick)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        observeLoading()
        initRecyclerView()
        mViewBinding.swipeContainer.setOnRefreshListener {
            viewModel.refreshNews()
        }
        observeError()
        viewModel.loadNews()
    }

    private fun initRecyclerView() {
        mViewBinding.rvFeed.apply {
            layoutManager = LinearLayoutManager(this@FeedActivity)
            adapter = feedAdapter
            itemAnimator = DefaultItemAnimator()
            val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback {
                viewModel.deleteNewsById(feedAdapter.getItemIdByPosition(it.adapterPosition))
            })
            itemTouchHelper.attachToRecyclerView(this)
        }
        observeNews()
    }

    private fun observeNews() {
        viewModel.news.observe(this, newsStateObserver)
    }

    private fun observeError() {
        viewModel.error.observe(this) {
            toast(R.string.load_news_error, Toast.LENGTH_LONG)
        }
    }

    private fun observeLoading() {
        viewModel.networkState.observe(this) {
            when (it.status) {
                Status.INITIAL_LOADED -> mViewBinding.indeterminateBar.gone()
                Status.INITIAL_LOADING -> mViewBinding.indeterminateBar.visible()
                Status.REFRESH_LOADED -> mViewBinding.swipeContainer.isRefreshing = false
                else -> feedAdapter.setState(it.status)
            }
        }
    }

    private val newsStateObserver = Observer<PagedList<News>> { list ->
        feedAdapter.submitList(list) {
            val layoutManager = (mViewBinding.rvFeed.layoutManager as LinearLayoutManager)
            val position = layoutManager.findFirstCompletelyVisibleItemPosition()
            if (position != RecyclerView.NO_POSITION) {
                mViewBinding.rvFeed.scrollToPosition(position)
            }
        }
    }

    private fun goToDetail(url: String) {
        if (url.isNotEmpty()) {
            startNewActivity(WebViewActivity::class) {
                putExtra(URL_EXTRA, url)
            }
        } else {
            toast(R.string.no_url)
        }
    }

    private fun onFavoriteClick(id: String, isFavorite: Boolean) {
        viewModel.updateFavoriteItem(id, isFavorite)
    }

    companion object {
        const val URL_EXTRA = "url_extra"
    }
}
