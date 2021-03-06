package com.ericktijerou.hackernews.presentation.ui.feed

import androidx.lifecycle.*
import com.ericktijerou.hackernews.core.ActionType
import com.ericktijerou.hackernews.domain.interactor.NewsInteractor
import com.ericktijerou.hackernews.presentation.ui.util.CoroutinesViewModel

class FeedViewModel(
    private val interactor: NewsInteractor
) : CoroutinesViewModel() {

    private val _error = MutableLiveData<Throwable>()
    private val _news = MutableLiveData<Int>()

    val error: LiveData<Throwable> get() = _error

    private val result = Transformations.map(_news) { interactor.getNewsList(it, viewModelScope) }
    val news = result.switchMap {
        it.pagedList
    }
    val networkState = result.switchMap { it.networkState }


    fun loadNews() {
        _news.postValue(ActionType.LOAD)
    }

    fun refreshNews() {
        deleteAllNews()
        _news.postValue(ActionType.REFRESH)
    }

    fun deleteAllNews() {
        launch {
            interactor.deleteAllNews()
        }
    }

    fun deleteNewsById(id: String) {
        launch {
            interactor.deleteNewsById(id)
        }
    }

    fun updateFavoriteItem(id: String, isFavorite: Boolean) {
        launch {
            interactor.setFavoriteNews(id, isFavorite)
        }
    }
}
