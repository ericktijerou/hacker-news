package com.ericktijerou.hackernews.data.repository

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ericktijerou.hackernews.data.cache.NewsDataStore
import com.ericktijerou.hackernews.data.entity.toDomain
import com.ericktijerou.hackernews.data.network.NewsCloudStore
import com.ericktijerou.hackernews.data.util.BoundaryCondition
import com.ericktijerou.hackernews.domain.entity.Listing
import com.ericktijerou.hackernews.domain.entity.News
import com.ericktijerou.hackernews.domain.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope

class NewsRepositoryImp(
    private val cloudStore: NewsCloudStore,
    private val dataStore: NewsDataStore,
    private val scope: CoroutineScope
) : NewsRepository {

    override fun getNewsList(actionType: Int): Listing<News> {
        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(5)
            .setInitialLoadSizeHint(PER_PAGE_COUNT)
            .setPageSize(PER_PAGE_COUNT)
            .build()

        val dataSourceFactory = dataStore.getNewsList().map { it.toDomain() }
        val boundaryCallback = BoundaryCondition(
            actionType,
            cloudStore,
            dataStore,
            PER_PAGE_COUNT * 2,
            scope
        )

        val data = LivePagedListBuilder(dataSourceFactory, config)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return Listing(
            pagedList = data,
            networkState = boundaryCallback.networkState
        )
    }

    override suspend fun deleteNewsById(id: String) {
        dataStore.deleteById(id)
    }

    override suspend fun deleteAllNews() {
        dataStore.deleteAll()
    }

    companion object {
        const val PER_PAGE_COUNT = 15
    }
}