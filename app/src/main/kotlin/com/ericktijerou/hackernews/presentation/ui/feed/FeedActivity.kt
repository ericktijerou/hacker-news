package com.ericktijerou.hackernews.presentation.ui.feed

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.*
import com.ericktijerou.hackernews.R
import com.ericktijerou.hackernews.core.*
import com.ericktijerou.hackernews.databinding.ActivityFeedBinding
import com.ericktijerou.hackernews.domain.entity.News
import com.ericktijerou.hackernews.presentation.ui.detail.WebViewActivity
import com.ericktijerou.hackernews.presentation.ui.util.BaseActivity
import com.ericktijerou.hackernews.presentation.ui.util.observe
import com.ericktijerou.hackernews.presentation.ui.util.startNewActivity
import com.ericktijerou.hackernews.presentation.ui.util.toast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class FeedActivity : BaseActivity<ActivityFeedBinding>() {

    override fun getViewBinding(): ActivityFeedBinding = ActivityFeedBinding.inflate(layoutInflater)

    private val viewModel by viewModel<FeedViewModel>()

    private val feedAdapter by lazy {
        FeedPagedListAdapter(::goToDetail, ::onFavoriteClick)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        observeLoading()
        initRecyclerView()
        initRefreshListener()
        viewModel.loadNews()
    }

    private fun initRefreshListener() {
        mViewBinding.swipeContainer.setOnRefreshListener {
            hideErrorView()
            viewModel.refreshNews()
        }
    }

    private fun initRecyclerView() {
        mViewBinding.rvFeed.apply {
            layoutManager = LinearLayoutManager(this@FeedActivity)
            adapter = feedAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(
                DividerItemDecoration(
                    applicationContext,
                    DividerItemDecoration.VERTICAL
                )
            )
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

    private fun observeLoading() {
        viewModel.networkState.observe(this) {
            when (it.status) {
                Status.INITIAL_LOADED -> endLoaderAnimate()
                Status.INITIAL_LOADING -> startLoaderAnimate()
                Status.REFRESH_LOADED -> hideSwipeRefresh()
                Status.FAILED -> {
                    showError(it.error)
                    feedAdapter.setState(it.status)
                }
                else -> feedAdapter.setState(it.status)
            }
        }
    }

    private fun endLoaderAnimate() {
        mViewBinding.lottieLoading.cancelAnimation()
        mViewBinding.lottieLoading.gone()
    }

    private fun startLoaderAnimate() {
        mViewBinding.lottieLoading.playAnimation()
        mViewBinding.lottieLoading.visible()
    }

    private fun hideSwipeRefresh() {
        mViewBinding.swipeContainer.isRefreshing = false
    }

    private fun showError(e: Error?) {
        val resId = when (e) {
            is Error.NotFound -> R.string.no_news
            is Error.Network -> R.string.no_internet
            else -> R.string.load_news_error
        }
        endLoaderAnimate()
        hideSwipeRefresh()
        if (feedAdapter.itemCount != 0) {
            toast(resId, Toast.LENGTH_SHORT)
        } else {
            showErrorView(resId)
        }
    }

    private fun showErrorView(@StringRes resId: Int) {
        mViewBinding.errorInclude.run {
            errorNetworkGroup.visible()
            lottieView.playAnimation()
            tvError.setText(resId)
        }

    }

    private fun hideErrorView() {
        mViewBinding.errorInclude.run {
            errorNetworkGroup.gone()
            lottieView.cancelAnimation()
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
