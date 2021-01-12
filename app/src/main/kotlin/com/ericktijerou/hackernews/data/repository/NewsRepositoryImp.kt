package com.ericktijerou.hackernews.data.repository

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ericktijerou.hackernews.core.NetworkConnectivity
import com.ericktijerou.hackernews.data.cache.NewsDataStore
import com.ericktijerou.hackernews.data.entity.toDomain
import com.ericktijerou.hackernews.data.factory.NewsDataSourceFactory
import com.ericktijerou.hackernews.domain.entity.Listing
import com.ericktijerou.hackernews.domain.entity.News
import com.ericktijerou.hackernews.domain.repository.NewsRepository

class NewsRepositoryImp(
    private val newsDataStore: NewsDataStore,
    private val newsDataSourceFactory: NewsDataSourceFactory,
    private val networkConnectivity: NetworkConnectivity
) : NewsRepository {

    @MainThread
    override fun getNewsList(): Listing<News> {
        val livePagedList = if (networkConnectivity.isInternetOn()) {
            getDataFromRemote()
        } else {
            getDataFromLocal()
        }
        val refreshState = newsDataSourceFactory.liveData.switchMap {
            it.initialLoad
        }
        return Listing(
            pagedList = livePagedList,
            loadingState = newsDataSourceFactory.liveData.switchMap {
                it.loadingState
            },
            refresh = {
                newsDataSourceFactory.liveData.value?.invalidate()
            },
            refreshState = refreshState
        )
    }


    private fun getDataFromRemote(): LiveData<PagedList<News>> {
        return LivePagedListBuilder(
            newsDataSourceFactory,
            NewsDataSourceFactory.pagedListConfig()
        ).build()
    }

    private fun getDataFromLocal(): LiveData<PagedList<News>> {
        val dataSourceFactory = newsDataStore.getNewsList().map { it.toDomain() }
        return LivePagedListBuilder(
            dataSourceFactory,
            NewsDataSourceFactory.pagedListConfig()
        ).build()
    }
}