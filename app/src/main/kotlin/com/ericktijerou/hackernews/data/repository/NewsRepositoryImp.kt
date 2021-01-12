package com.ericktijerou.hackernews.data.repository

import com.ericktijerou.hackernews.data.cache.NewsDataStore
import com.ericktijerou.hackernews.data.entity.toDomain
import com.ericktijerou.hackernews.data.network.NewsCloudStore
import com.ericktijerou.hackernews.domain.entity.News
import com.ericktijerou.hackernews.domain.repository.NewsRepository

class NewsRepositoryImp(
    private val cloudStore: NewsCloudStore,
    private val newsDataStore: NewsDataStore
) : NewsRepository {

    override suspend fun getNewsList(refresh: Boolean): List<News> {
        val newsLocalList = getNewsListFromLocal()
        return if (newsLocalList.isNotEmpty() &&  !refresh) {
            if (newsDataStore.isExpired()) {
                getNewsListFromRemote()
            } else {
                newsLocalList
            }
        } else {
            getNewsListFromRemote()
        }
    }

    private suspend fun getNewsListFromRemote(): List<News> {
        val newsFromRemote = cloudStore.getNewsList()
        return if (newsFromRemote.isNotEmpty()) {
            newsDataStore.saveNewsList(newsFromRemote).map { it.toDomain() }
        } else {
            getNewsListFromLocal()
        }
    }

    private suspend fun getNewsListFromLocal(): List<News> {
        return newsDataStore.getNewsList().map { it.toDomain() }
    }

    override suspend fun getNewsById(newsId: Long): News {
        return newsDataStore.getNewsById(newsId).toDomain()
    }
}