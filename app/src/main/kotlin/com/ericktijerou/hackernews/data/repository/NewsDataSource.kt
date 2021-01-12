package com.ericktijerou.hackernews.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.ericktijerou.hackernews.data.cache.NewsDataStore
import com.ericktijerou.hackernews.data.entity.toDomain
import com.ericktijerou.hackernews.data.network.NewsCloudStore
import com.ericktijerou.hackernews.domain.entity.News
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class NewsDataSource(
    private val cloudStore: NewsCloudStore,
    private val localStore: NewsDataStore,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, News>() {

    val state = MutableLiveData<Int>()

    val initialLoad = MutableLiveData<Int>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, News>
    ) {
        fetchData(0, params.requestedLoadSize) {
            callback.onResult(it, null, 1)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        val page = params.key
        fetchData(page, params.requestedLoadSize) {
            callback.onResult(it, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        // Ignored, since we only ever append to our initial load
    }

    private fun fetchData(page: Int, pageSize: Int, callback: (List<News>) -> Unit) {
        scope.launch(getJobErrorHandler()) {
            val response = cloudStore.fetchNewsList(page, pageSize)
            if (response.isNotEmpty()) {
                localStore.insertNewsList(response)
                callback(response.map { it.toDomain() })
            }
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        postError(e.toString())
    }

    private fun postError(message: String) {
        Timber.e("An error happened: $message")
    }
}