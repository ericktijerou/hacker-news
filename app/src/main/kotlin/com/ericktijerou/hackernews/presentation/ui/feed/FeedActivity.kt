package com.ericktijerou.hackernews.presentation.ui.feed

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ericktijerou.hackernews.R
import com.ericktijerou.hackernews.core.gone
import com.ericktijerou.hackernews.core.visible
import com.ericktijerou.hackernews.databinding.ActivityFeedBinding
import com.ericktijerou.hackernews.domain.entity.News
import com.ericktijerou.hackernews.presentation.ui.BaseActivity
import com.ericktijerou.hackernews.presentation.ui.util.observe
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class FeedActivity : BaseActivity<ActivityFeedBinding>() {

    override fun getViewBinding(): ActivityFeedBinding = ActivityFeedBinding.inflate(layoutInflater)

    private val viewModel by viewModel<FeedViewModel>()

    private val feedAdapter by lazy {
        FeedPagedListAdapter(::goToStory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        mViewBinding.apply {
            rvFeed.apply {
                layoutManager = LinearLayoutManager(this@FeedActivity)
                adapter = feedAdapter
            }
        }
        observeLoading()
        observeNews()
        observeError()
        viewModel.refreshFeed()
    }

    private fun observeNews() {
        viewModel.news.observe(this, newsStateObserver)
    }

    private fun observeError() {
        viewModel.error.observe(this) {
            Timber.e(it)
            Toast.makeText(
                this@FeedActivity,
                R.string.load_news_error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun observeLoading() {
        viewModel.loading.observe(this) {
            mViewBinding.indeterminateBar.visible(it)
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

    private fun goToStory(view: View, newsId: Long) {
        Timber.d("newsId = $newsId")
    }

    companion object {
        const val NEWS_ID = "news-id"
    }

}
