package com.ericktijerou.hackernews.data.cache

import androidx.paging.DataSource
import com.ericktijerou.hackernews.data.cache.dao.NewsDao
import com.ericktijerou.hackernews.data.cache.entity.NewsEntity
import com.ericktijerou.hackernews.data.cache.entity.toData
import com.ericktijerou.hackernews.data.cache.system.PreferencesHelper
import com.ericktijerou.hackernews.data.entity.NewsModel
import com.ericktijerou.hackernews.data.entity.toLocal

class NewsDataStore(
    private val newsDao: NewsDao,
    private val preferencesHelper: PreferencesHelper
) {

    companion object {
        private const val EXPIRATION_TIME = (60 * 10 * 1000).toLong()
    }

    fun getNewsList(): DataSource.Factory<Int, NewsModel> {
        return newsDao.getAll().map { it.toData() }
    }

    suspend fun getNewsById(newsId: Long): NewsModel {
        return newsDao.getNewsById(newsId).toData()
    }

    suspend fun saveNewsList(newsList: List<NewsModel>) {
        newsDao.deleteAll()
        newsDao.insertAll(newsList.map { it.toLocal() })
        setLastCacheTime(System.currentTimeMillis())
    }

    suspend fun insertNewsList(newsList: List<NewsModel>) {
        newsDao.insertAll(newsList.map { it.toLocal() })
        setLastCacheTime(System.currentTimeMillis())
    }

    private fun setLastCacheTime(lastCache: Long) {
        preferencesHelper.lastCacheTime = lastCache
    }

    private fun getLastCacheUpdateTimeMillis(): Long {
        return preferencesHelper.lastCacheTime
    }

    fun isExpired(): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastUpdateTime = this.getLastCacheUpdateTimeMillis()
        return currentTime - lastUpdateTime > EXPIRATION_TIME
    }
}