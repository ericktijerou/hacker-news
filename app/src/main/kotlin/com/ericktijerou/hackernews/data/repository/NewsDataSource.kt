package com.ericktijerou.hackernews.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.ericktijerou.hackernews.core.ActionType
import com.ericktijerou.hackernews.core.LoadingState
import com.ericktijerou.hackernews.data.cache.NewsDataStore
import com.ericktijerou.hackernews.data.entity.toDomain
import com.ericktijerou.hackernews.data.network.NewsCloudStore
import com.ericktijerou.hackernews.domain.entity.News
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NewsDataSource(
    private val cloudStore: NewsCloudStore,
    private val localStore: NewsDataStore,
    private val scope: CoroutineScope,
    private val actionType: Int
) : PageKeyedDataSource<Int, News>() {

    val progressState = MutableLiveData<Int>()

    val initialLoad = MutableLiveData<Int>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, News>
    ) {
        val loadingState = when (actionType) {
            ActionType.REFRESH -> LoadingState.REFRESH_LOADING
            else -> LoadingState.INITIAL_LOADING
        }
        progressState.postValue(loadingState)
        scope.launch(getJobErrorHandler()) {
            localStore.deleteAll()
            fetchData(0, params.requestedLoadSize) {
                callback.onResult(it, null, 1)
                val loadedState = when (actionType) {
                    ActionType.REFRESH -> LoadingState.REFRESH_LOADED
                    else -> LoadingState.INITIAL_LOADED
                }
                progressState.postValue(loadedState)
            }
        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        progressState.postValue(LoadingState.LOADING)
        val page = params.key
        scope.launch(getJobErrorHandler()) {
            fetchData(page, params.requestedLoadSize) {
                callback.onResult(it, page + 1)
                progressState.postValue(LoadingState.LOADED)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        // Ignored, since we only ever append to our initial load
    }

    private suspend fun fetchData(page: Int, pageSize: Int, callback: (List<News>) -> Unit) {
        val response = cloudStore.fetchNewsList(page, pageSize)
        if (response.isNotEmpty()) {
            localStore.insertNewsList(response)
            callback(response.map { it.toDomain() })
        }
    }

    private fun getJobErrorHandler() = CoroutineExceptionHandler { _, e ->
        postError(e.toString())
    }

    private fun postError(message: String) {
    }
}