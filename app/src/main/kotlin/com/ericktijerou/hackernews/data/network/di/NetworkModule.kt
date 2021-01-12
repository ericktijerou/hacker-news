package com.ericktijerou.hackernews.data.network.di

import com.ericktijerou.hackernews.core.NetworkConnectivity
import com.ericktijerou.hackernews.data.network.NewsCloudStore
import com.ericktijerou.hackernews.data.network.api.HackerNewsApi
import com.ericktijerou.hackernews.data.network.util.buildOkHttpClient
import com.ericktijerou.hackernews.data.network.util.buildRetrofit
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

const val NAMED = "NEWS"
const val NAMED_CLIENT = "NEWS_CLIENT"

internal val networkModule = module {
    single(named(NAMED_CLIENT)) { createHttpClient() }
    single(named(NAMED)) { createRetrofit(get(named(NAMED_CLIENT))) }
    single { provideApi(get(named(NAMED))) }
    single { NetworkConnectivity(get()) }
    single { NewsCloudStore(get(), get()) }
}

private fun createHttpClient(): OkHttpClient {
    return buildOkHttpClient()
}

private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return buildRetrofit(okHttpClient)
}

private fun provideApi(retrofit: Retrofit): HackerNewsApi {
    return retrofit.create(HackerNewsApi::class.java)
}