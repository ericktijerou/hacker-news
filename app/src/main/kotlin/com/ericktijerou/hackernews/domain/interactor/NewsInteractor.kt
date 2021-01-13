package com.ericktijerou.hackernews.domain.interactor

import com.ericktijerou.hackernews.domain.entity.Listing
import com.ericktijerou.hackernews.domain.entity.News

interface NewsInteractor {
    fun getNewsList(actionType: Int): Listing<News>
    suspend fun deleteNewsById(id: String)
    suspend fun deleteAllNews()
}
