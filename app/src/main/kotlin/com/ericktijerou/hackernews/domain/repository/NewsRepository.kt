package com.ericktijerou.hackernews.domain.repository

import com.ericktijerou.hackernews.domain.entity.News

interface NewsRepository {
    suspend fun getNewsList(refresh: Boolean): List<News>
    suspend fun getNewsById(newsId: Long): News
}