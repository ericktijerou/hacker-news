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

    private val _loading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<Throwable>()

    val loading: LiveData<Boolean> get() = _loading
    val error: LiveData<Throwable> get() = _error

    private val liveData = MutableLiveData<Long>()

    private val result = Transformations.map(liveData) {
        interactor.getNewsList()
    }

    val news = result.switchMap { it.pagedList }

    fun load(delay: Long) {
        liveData.value = delay
    }

    fun getFeed(refresh: Boolean = false) {
 /*       launch {
            try {
                _loading.value = true
                _news.value = interactor.getNewsList(refresh)
            } catch (exception: Exception) {
                _error.value = exception
            } finally {
                _loading.value = false
            }
        }*/
    }

}
