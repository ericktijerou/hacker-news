package com.ericktijerou.hackernews.presentation.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.switchMap
import com.ericktijerou.hackernews.domain.interactor.NewsInteractor
import com.ericktijerou.hackernews.presentation.ui.CoroutinesViewModel

class FeedViewModel(
    private val interactor: NewsInteractor
) : CoroutinesViewModel() {

    private val _error = MutableLiveData<Throwable>()
    private val _news = MutableLiveData<Long>()

    val error: LiveData<Throwable> get() = _error

    private val result = Transformations.map(_news) { interactor.getNewsList() }
    val news = result.switchMap {
        it.pagedList
    }
    val loadingState = result.switchMap { it.loadingState }


    fun load() {
        _news.value = 0
    }

    fun refreshFeed() {
        try {
            _news.value = 0
        } catch (exception: Exception) {
            _error.value = exception
        }
    }
}
