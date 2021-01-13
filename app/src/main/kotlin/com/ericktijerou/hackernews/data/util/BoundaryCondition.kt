package com.ericktijerou.hackernews.data.util

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ericktijerou.hackernews.core.ActionType
import com.ericktijerou.hackernews.core.Error
import com.ericktijerou.hackernews.core.NetworkException
import com.ericktijerou.hackernews.core.NetworkState
import com.ericktijerou.hackernews.core.NotFoundException
import com.ericktijerou.hackernews.data.cache.NewsDataStore
import com.ericktijerou.hackernews.data.entity.NewsModel
import com.ericktijerou.hackernews.data.network.NewsCloudStore
import com.ericktijerou.hackernews.domain.entity.News
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

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
                    val loadedState = when {
                        lastRequestedPage == 0 && actionType == ActionType.LOAD -> NetworkState.INITIAL_LOADED
                        lastRequestedPage == 0 && actionType == ActionType.REFRESH -> NetworkState.REFRESH_LOADED
                        else -> NetworkState.LOADED
                    }
                    networkState.postValue(loadedState)
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
