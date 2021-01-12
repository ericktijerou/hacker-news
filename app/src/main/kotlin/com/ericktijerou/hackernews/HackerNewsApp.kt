package com.ericktijerou.hackernews

import android.app.Application
import com.ericktijerou.hackernews.BuildConfig.DEBUG
import com.ericktijerou.hackernews.core.listByElementsOf
import com.ericktijerou.hackernews.data.di.dataModules
import com.ericktijerou.hackernews.domain.di.domainModules
import com.ericktijerou.hackernews.presentation.di.presentationModules
import com.ericktijerou.hackernews.presentation.ui.feed.FeedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import timber.log.Timber

open class HackerNewsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if(DEBUG) Timber.plant(Timber.DebugTree())
        startKoin()
    }

    private fun startKoin() {
        org.koin.core.context.startKoin {
            androidLogger()
            androidContext(this@HackerNewsApp)
            modules(modules)
        }
    }

    private val modules by lazy {
        listByElementsOf<Module>(
            dataModules,
            domainModules,
            presentationModules,
            appModule
        )
    }

    private val appModule = module {
        factory { CoroutineScope(Dispatchers.IO) }
    }
}
