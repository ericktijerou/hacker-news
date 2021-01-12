package com.ericktijerou.hackernews.data.network

import com.ericktijerou.hackernews.core.NetworkConnectivity
import com.ericktijerou.hackernews.core.NotFoundException
import com.ericktijerou.hackernews.data.entity.NewsModel
import com.ericktijerou.hackernews.data.network.api.HackerNewsApi
import com.ericktijerou.hackernews.data.network.entity.toData

class NewsCloudStore(private val api: HackerNewsApi,
                     private val networkConnectivity: NetworkConnectivity
) {

    suspend fun getNewsList(): List<NewsModel> {
        return if (networkConnectivity.isInternetOn()) {
            api.getNewsList().let { response ->
                response.body()?.hits?.map { it.toData() } ?: throw NotFoundException()
            }
        } else {
            emptyList()
        }
    }
}