package com.ericktijerou.hackernews.data.repository

import androidx.lifecycle.switchMap
import androidx.paging.LivePagedListBuilder
import com.ericktijerou.hackernews.core.NetworkConnectivity
import com.ericktijerou.hackernews.data.cache.NewsDataStore
import com.ericktijerou.hackernews.data.entity.toDomain
import com.ericktijerou.hackernews.data.factory.NewsDataSourceFactory
import com.ericktijerou.hackernews.data.network.NewsCloudStore
import com.ericktijerou.hackernews.domain.entity.Listing
import com.ericktijerou.hackernews.domain.entity.News
import com.ericktijerou.hackernews.domain.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope

class NewsRepositoryImp(
    private val cloudStore: NewsCloudStore,
    private val dataStore: NewsDataStore,
    private val scope: CoroutineScope,
    private val networkConnectivity: NetworkConnectivity
) : NewsRepository {

    override fun getNewsList(actionType: Int): Listing<News> {
        val factory = NewsDataSourceFactory(cloudStore, dataStore, scope, actionType)
        val dataSourceFactory = if (networkConnectivity.isInternetOn()) {
            factory
        } else {
            dataStore.getNewsList().map { it.toDomain() }
        }

        val livePagedList =
            LivePagedListBuilder(dataSourceFactory, NewsDataSourceFactory.pagedListConfig()).build()

        val refreshState = factory.liveData.switchMap {
            it.initialLoad
        }
        return Listing(
            pagedList = livePagedList,
            loadingState = factory.liveData.switchMap {
                it.progressState
            },
            refresh = {
                factory.liveData.value?.invalidate()
            },
            refreshState = refreshState
        )
    }

    override suspend fun deleteNewsById(id: String) {
        dataStore.deleteById(id)
    }
}