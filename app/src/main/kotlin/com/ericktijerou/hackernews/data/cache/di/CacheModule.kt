package com.ericktijerou.hackernews.data.cache.di

import com.ericktijerou.hackernews.data.cache.NewsDataStore
import com.ericktijerou.hackernews.data.cache.system.PreferencesHelper
import com.ericktijerou.hackernews.data.cache.system.SystemDatabase
import org.koin.dsl.module

internal val cacheModule = module {
    single { SystemDatabase.newInstance(get()) }
    single { get<SystemDatabase>().newsDao() }
    single { PreferencesHelper(get()) }
    single { NewsDataStore(get(), get()) }
}