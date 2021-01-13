package com.ericktijerou.hackernews.presentation.ui.detail

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.MenuItem
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ericktijerou.hackernews.core.NetworkConnectivity
import com.ericktijerou.hackernews.core.gone
import com.ericktijerou.hackernews.core.visible
import com.ericktijerou.hackernews.databinding.ActivityDetailBinding
import com.ericktijerou.hackernews.presentation.ui.util.BaseActivity
import com.ericktijerou.hackernews.presentation.ui.feed.FeedActivity.Companion.URL_EXTRA
import org.koin.android.ext.android.inject

class WebViewActivity : BaseActivity<ActivityDetailBinding>() {

    private val url by lazy { intent.getStringExtra(URL_EXTRA).orEmpty() }
    private var isAlreadyCreated = false
    val networkConnectivity : NetworkConnectivity by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = Uri.parse(url).host
        }

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

    override fun getViewBinding(): ActivityDetailBinding =
        ActivityDetailBinding.inflate(layoutInflater)

    override fun onResume() {
        super.onResume()
        if (isAlreadyCreated && !networkConnectivity.isInternetOn()) {
            isAlreadyCreated = false
            showErrorDialog(
                "Error", "No internet connection. Please check your connection.",
                this@WebViewActivity
            )
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && mViewBinding.webView.canGoBack()) {
            mViewBinding.webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showErrorDialog(title: String, message: String, context: Context) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setNegativeButton("Cancel") { _, _ ->
            this@WebViewActivity.finish()
        }
        dialog.setNeutralButton("Settings") { _, _ ->
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
        dialog.setPositiveButton("Retry") { _, _ ->
            this@WebViewActivity.recreate()
        }
        dialog.create().show()
    }

    private fun endLoaderAnimate() {
        mViewBinding.lottieLoading.gone()
    }

    private fun startLoaderAnimate() {
        mViewBinding.lottieLoading.visible()
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