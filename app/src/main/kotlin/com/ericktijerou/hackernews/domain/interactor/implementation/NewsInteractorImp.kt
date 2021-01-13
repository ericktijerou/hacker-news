package com.ericktijerou.hackernews.domain.interactor.implementation

import com.ericktijerou.hackernews.domain.entity.Listing
import com.ericktijerou.hackernews.domain.entity.News
import com.ericktijerou.hackernews.domain.interactor.NewsInteractor
import com.ericktijerou.hackernews.domain.repository.NewsRepository

class NewsInteractorImp(
    private val repository: NewsRepository
) : NewsInteractor {

    override fun getNewsList(actionType: Int): Listing<News> {
        return repository.getNewsList(actionType)
    }

    override suspend fun deleteNewsById(id: String) {
        return repository.deleteNewsById(id)
    }
}