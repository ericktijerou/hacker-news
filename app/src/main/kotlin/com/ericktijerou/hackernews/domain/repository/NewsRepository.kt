package com.ericktijerou.hackernews.domain.repository

import com.ericktijerou.hackernews.domain.entity.Listing
import com.ericktijerou.hackernews.domain.entity.News
import kotlinx.coroutines.CoroutineScope

interface NewsRepository {
    fun getNewsList(actionType: Int, coroutineScope: CoroutineScope): Listing<News>
    suspend fun deleteNewsById(id: String)
    suspend fun deleteAllNews()
    suspend fun setFavoriteNews(id: String, isFavorite: Boolean)
}