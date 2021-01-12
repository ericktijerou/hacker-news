package com.ericktijerou.hackernews.presentation.di

import com.ericktijerou.hackernews.core.listByElementsOf
import com.ericktijerou.hackernews.presentation.ui.feed.FeedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal val presentationModules by lazy {
    listByElementsOf<Module>(
        viewModelModule
    )
}

internal val viewModelModule = module {
    viewModel { FeedViewModel(get()) }
}