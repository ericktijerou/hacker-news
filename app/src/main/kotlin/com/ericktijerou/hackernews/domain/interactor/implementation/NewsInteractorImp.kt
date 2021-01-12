package com.ericktijerou.hackernews.domain.interactor.implementation

import com.ericktijerou.hackernews.domain.entity.News
import com.ericktijerou.hackernews.domain.interactor.NewsInteractor
import com.ericktijerou.hackernews.domain.repository.NewsRepository

class NewsInteractorImp(
    private val repository: NewsRepository
) : NewsInteractor {
    override suspend fun getNewsList(refresh: Boolean): List<News> {
        return repository.getNewsList(refresh)
    }

    override suspend fun getNewsById(id: Long): News {
        return repository.getNewsById(id)
    }
}