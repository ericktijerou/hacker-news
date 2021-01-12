package com.ericktijerou.hackernews.presentation.ui.feed

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ericktijerou.hackernews.R
import com.ericktijerou.hackernews.presentation.ui.util.observe
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import com.ericktijerou.hackernews.databinding.ActivityFeedBinding as Binding

class FeedActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<Binding>(this, R.layout.activity_feed)
    }

    private val viewModel by viewModel<FeedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.let { activity ->
            binding.apply {
                lifecycleOwner = activity
                viewModel = activity.viewModel
                rvFeed.adapter = FeedAdapter(::goToStory)
            }
        }
        observeNews()
        observeError()
    }

    private fun observeNews() {
        viewModel.news.observe(this) { news ->
            Timber.d("news = $news")
            binding.rvFeed.adapter?.let {
                (it as FeedAdapter).updateNewsList(news)
            }
        }
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

    private fun goToStory(view: View, newsId: Long) {
        Timber.d("newsId = $newsId")
    }

    companion object {
        const val NEWS_ID = "news-id"
    }

}
