package com.ericktijerou.hackernews.domain.interactor

import com.ericktijerou.hackernews.domain.entity.Listing
import com.ericktijerou.hackernews.domain.entity.News
import kotlinx.coroutines.CoroutineScope

interface NewsInteractor {
    fun getNewsList(actionType: Int, coroutineScope: CoroutineScope): Listing<News>
    suspend fun deleteNewsById(id: String)
    suspend fun deleteAllNews()
    suspend fun setFavoriteNews(id: String, isFavorite: Boolean)
}
