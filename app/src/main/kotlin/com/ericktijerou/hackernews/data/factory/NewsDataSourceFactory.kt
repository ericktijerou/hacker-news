package com.ericktijerou.hackernews.data.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.ericktijerou.hackernews.data.cache.NewsDataStore
import com.ericktijerou.hackernews.data.network.NewsCloudStore
import com.ericktijerou.hackernews.data.repository.NewsDataSource
import com.ericktijerou.hackernews.domain.entity.News
import kotlinx.coroutines.CoroutineScope

class NewsDataSourceFactory (
    private val cloudStore: NewsCloudStore,
    private val dataStore: NewsDataStore,
    private val scope: CoroutineScope
) : DataSource.Factory<Int, News>() {

    val liveData = MutableLiveData<NewsDataSource>()

    override fun create(): DataSource<Int, News> {
        val source = NewsDataSource(
            cloudStore,
            dataStore,
            scope
        )
        liveData.postValue(source)
        return source
    }

    companion object {
        private const val PAGE_SIZE = 15

        fun pagedListConfig() = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
    }
}