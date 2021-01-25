package com.ericktijerou.hackernews.data.util

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ericktijerou.hackernews.core.*
import com.ericktijerou.hackernews.data.cache.NewsDataStore
import com.ericktijerou.hackernews.data.network.NewsCloudStore
import com.ericktijerou.hackernews.domain.entity.News
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class BoundaryCondition(
    private val actionType: Int,
    private val service: NewsCloudStore,
    private val cache: NewsDataStore,
    private val pageSize: Int,
    private val scope: CoroutineScope
) : PagedList.BoundaryCallback<News>() {

    val networkState = MutableLiveData<NetworkState>(NetworkState.LOADED)

    private var lastRequestedPage = 0

    override fun onZeroItemsLoaded() {
        if (actionType == ActionType.LOAD) networkState.postValue(NetworkState.INITIAL_LOADING)
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: News) {
        requestAndSaveData()
    }

    override fun onItemAtFrontLoaded(itemAtFront: News) {
        networkState.postValue(NetworkState.LOADED)
    }

    private fun requestAndSaveData() {
        scope.launch(handler) {
            if (networkState.value != NetworkState.LOADING) {
                if (lastRequestedPage != 0) networkState.postValue(NetworkState.LOADING)
                val newsList = service.fetchNewsList(lastRequestedPage, pageSize)
                if (newsList.isNotEmpty()) {
                    val favorites = cache.getFavoriteNews()
                    favorites.forEach {
                        newsList.find { newsId -> newsId.id == it }?.isFavorite = true
                    }
                    cache.insertNewsList(newsList)
                    if (actionType == ActionType.LOAD) networkState.postValue(NetworkState.INITIAL_LOADED)
                    else if (actionType == ActionType.REFRESH) networkState.postValue(NetworkState.REFRESH_LOADED)
                    lastRequestedPage++
                }
            }
        }
    }


    private val handler = CoroutineExceptionHandler { _, exception ->
        val error = when (exception) {
            is NetworkException -> Error.Network
            is NotFoundException -> Error.NotFound
            else -> Error.Unknown
        }
        networkState.postValue(NetworkState.error(error))
    }
}
