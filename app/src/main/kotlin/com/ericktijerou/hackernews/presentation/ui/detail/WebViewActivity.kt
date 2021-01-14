package com.ericktijerou.hackernews.presentation.ui.detail

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.StringRes
import com.ericktijerou.hackernews.R
import com.ericktijerou.hackernews.core.NetworkConnectivity
import com.ericktijerou.hackernews.core.gone
import com.ericktijerou.hackernews.core.visible
import com.ericktijerou.hackernews.databinding.ActivityDetailBinding
import com.ericktijerou.hackernews.presentation.ui.util.BaseActivity
import com.ericktijerou.hackernews.presentation.ui.feed.FeedActivity.Companion.URL_EXTRA
import org.koin.android.ext.android.inject

class WebViewActivity : BaseActivity<ActivityDetailBinding>() {

    private val url by lazy { intent.getStringExtra(URL_EXTRA).orEmpty() }
    private val networkConnectivity : NetworkConnectivity by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        initToolbar()
        initView()
    }

    private fun initView() {
        if (networkConnectivity.isInternetOn()) {
            hideErrorView()
            initWebView()
        } else {
            showErrorView(R.string.no_internet)
        }
    }

    private fun initToolbar() {
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = Uri.parse(url).host
        }
    }

    private fun initWebView() {
        startLoaderAnimate()
        mViewBinding.webView.let {
            it.settings.javaScriptEnabled = true
            it.settings.setSupportZoom(false)
            it.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    endLoaderAnimate()
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    endLoaderAnimate()
                }
            }
            it.loadUrl(url)
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

    override fun getViewBinding(): ActivityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && mViewBinding.webView.canGoBack()) {
            mViewBinding.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun endLoaderAnimate() {
        mViewBinding.progressBar.gone()
    }

    private fun startLoaderAnimate() {
        mViewBinding.progressBar.visible()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}