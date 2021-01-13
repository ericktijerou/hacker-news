package com.ericktijerou.hackernews.data.cache

import androidx.paging.DataSource
import com.ericktijerou.hackernews.data.cache.dao.BlockNewsDao
import com.ericktijerou.hackernews.data.cache.dao.FavoriteNewsDao
import com.ericktijerou.hackernews.data.cache.dao.NewsDao
import com.ericktijerou.hackernews.data.cache.entity.BlockNewsEntity
import com.ericktijerou.hackernews.data.cache.entity.FavoriteNewsEntity
import com.ericktijerou.hackernews.data.cache.entity.toData
import com.ericktijerou.hackernews.data.cache.system.PreferencesHelper
import com.ericktijerou.hackernews.data.entity.NewsModel
import com.ericktijerou.hackernews.data.entity.toLocal

class NewsDataStore(
    private val newsDao: NewsDao,
    private val favoriteNewsDao: FavoriteNewsDao,
    private val blockNewsDao: BlockNewsDao,
    private val preferencesHelper: PreferencesHelper
) {

    companion object {
        private const val EXPIRATION_TIME = (60 * 10 * 1000).toLong()
    }

    fun getNewsList(): DataSource.Factory<Int, NewsModel> {
        return newsDao.getAll().map { it.toData() }
    }

    suspend fun deleteAll() {
        return newsDao.deleteAll()
    }

    suspend fun setFavoriteNews(id: String, isFavorite: Boolean) {
        newsDao.updateById(id, isFavorite)
        val favorite = FavoriteNewsEntity(id)
        if (isFavorite) {
            favoriteNewsDao.insertFavorites(favorite)
        } else {
            favoriteNewsDao.deleteFavorites(favorite)
        }
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

    suspend fun getFavoriteNews(): List<String> {
        return favoriteNewsDao.getAll().map { it.id }
    }

    suspend fun deleteById(id: String) {
        newsDao.deleteById(id)
        blockNewsDao.insertBlocks(BlockNewsEntity(id))
    }

    fun isExpired(): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastUpdateTime = this.getLastCacheUpdateTimeMillis()
        return currentTime - lastUpdateTime > EXPIRATION_TIME
    }
}