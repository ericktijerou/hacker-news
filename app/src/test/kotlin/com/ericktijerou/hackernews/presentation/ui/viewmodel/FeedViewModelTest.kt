package com.ericktijerou.hackernews.presentation.ui.viewmodel

import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.ericktijerou.hackernews.core.ActionType
import com.ericktijerou.hackernews.domain.entity.News
import com.ericktijerou.hackernews.domain.interactor.NewsInteractor
import com.ericktijerou.hackernews.mock.NewsMock
import com.ericktijerou.hackernews.presentation.ui.feed.FeedViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FeedViewModelTest: BaseViewModelTest() {
    private val interactor = mockk<NewsInteractor>()
    private val newsObserver = mockk<Observer<PagedList<News>>>()
    private val errorObserver = mockk<Observer<Throwable?>>()
    private val viewModel = FeedViewModel(interactor)

    @ExperimentalCoroutinesApi
    @Test
    fun `no interactions`() = runBlockingTest {
        every { newsObserver.onChanged(any()) } just Runs
        viewModel.news.observeForever(newsObserver)
        coVerify(exactly = 0) { interactor.getNewsList(ActionType.LOAD) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should set a favorites by id`() = runBlocking {
        val newsItem = NewsMock.news
        coEvery { interactor.setFavoriteNews(newsItem.id, newsItem.isFavorite) } returns Unit
        viewModel.updateFavoriteItem(newsItem.id, newsItem.isFavorite)
        coVerify { interactor.setFavoriteNews(newsItem.id, newsItem.isFavorite) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should delete a news by id`() = runBlocking {
        val newsItem = NewsMock.news
        coEvery { interactor.deleteNewsById(newsItem.id) } returns Unit
        viewModel.deleteNewsById(newsItem.id)
        coVerify { interactor.deleteNewsById(newsItem.id) }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should delete all news`() = runBlocking {
        coEvery { interactor.deleteAllNews() } returns Unit
        viewModel.deleteAllNews()
        coVerify { interactor.deleteAllNews() }
    }
}