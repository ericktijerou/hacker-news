package com.ericktijerou.hackernews.domain.interactor

import com.ericktijerou.hackernews.domain.entity.News

interface NewsInteractor {
    suspend fun getNewsList(refresh: Boolean): List<News>
    suspend fun getNewsById(id: Long): News
}
