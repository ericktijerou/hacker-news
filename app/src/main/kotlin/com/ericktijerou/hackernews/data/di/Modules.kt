package com.ericktijerou.hackernews.data.di

import com.ericktijerou.hackernews.core.listByElementsOf
import com.ericktijerou.hackernews.data.cache.di.cacheModule
import com.ericktijerou.hackernews.data.network.di.networkModule
import com.ericktijerou.hackernews.data.repository.di.repositoryModule
import org.koin.core.module.Module

internal val dataModules by lazy {
    listByElementsOf<Module>(
        cacheModule,
        networkModule,
        repositoriesModules
    )
}

internal val repositoriesModules by lazy {
    listByElementsOf<Module>(
        repositoryModule
    )
}
