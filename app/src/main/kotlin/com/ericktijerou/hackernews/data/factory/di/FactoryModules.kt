package com.ericktijerou.hackernews.data.factory.di

import com.ericktijerou.hackernews.data.factory.NewsDataSourceFactory
import org.koin.dsl.module

val factoryModule = module {
    single { NewsDataSourceFactory(get(), get(), get()) }
}