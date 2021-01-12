package com.ericktijerou.hackernews.presentation.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ericktijerou.hackernews.domain.entity.News
import com.ericktijerou.hackernews.domain.interactor.NewsInteractor
import com.ericktijerou.hackernews.presentation.ui.CoroutinesViewModel

class FeedViewModel(
    private val interactor: NewsInteractor
) : CoroutinesViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    private val _news = MutableLiveData<List<News>>()
    private val _error = MutableLiveData<Throwable>()

    val loading: LiveData<Boolean> get() = _loading
    val news: LiveData<List<News>> get() = _news
    val error: LiveData<Throwable> get() = _error

    init { getFeed() }

    fun getFeed(refresh: Boolean = false) {
        launch {
            try {
                _loading.value = true
                _news.value = interactor.getNewsList(refresh)
            } catch (exception: Exception) {
                _error.value = exception
            } finally {
                _loading.value = false
            }
        }
    }

}
