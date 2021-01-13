package com.ericktijerou.hackernews.domain.repository

import com.ericktijerou.hackernews.domain.entity.Listing
import com.ericktijerou.hackernews.domain.entity.News

interface NewsRepository {
    fun getNewsList(actionType: Int): Listing<News>
    suspend fun deleteNewsById(id: String)
    suspend fun deleteAllNews()
}