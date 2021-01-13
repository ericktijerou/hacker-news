package com.ericktijerou.hackernews.domain.interactor.implementation

import com.ericktijerou.hackernews.domain.entity.Listing
import com.ericktijerou.hackernews.domain.entity.News
import com.ericktijerou.hackernews.domain.interactor.NewsInteractor
import com.ericktijerou.hackernews.domain.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope

class NewsInteractorImp(
    private val repository: NewsRepository
) : NewsInteractor {

    override fun getNewsList(actionType: Int, coroutineScope: CoroutineScope): Listing<News> {
        return repository.getNewsList(actionType, coroutineScope)
    }

    override suspend fun deleteNewsById(id: String) {
        return repository.deleteNewsById(id)
    }

    override suspend fun deleteAllNews() {
        return repository.deleteAllNews()
    }

    override suspend fun setFavoriteNews(id: String, isFavorite: Boolean) {
        return repository.setFavoriteNews(id, isFavorite)
    }
}