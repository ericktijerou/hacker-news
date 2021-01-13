package com.ericktijerou.hackernews.domain

import com.ericktijerou.hackernews.domain.interactor.implementation.NewsInteractorImp
import com.ericktijerou.hackernews.domain.repository.NewsRepository
import com.ericktijerou.hackernews.mock.NewsMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class NewsInteractorTest {

    private val repository = mockk<NewsRepository>()
    private val interactor = NewsInteractorImp(repository)

    @Test
    fun `should set a favorites by id`() = runBlocking {
        val newsItem = NewsMock.news
        coEvery { repository.setFavoriteNews(newsItem.id, newsItem.isFavorite) } returns Unit
        interactor.setFavoriteNews(newsItem.id, newsItem.isFavorite)
        coVerify { repository.setFavoriteNews(newsItem.id, newsItem.isFavorite) }
    }


    @Test(expected = Exception::class)
    fun `should throw an exception when set a favorite by id`() = runBlocking {
        val newsItem = NewsMock.news
        coEvery { repository.setFavoriteNews(newsItem.id, newsItem.isFavorite) } throws Exception()
        interactor.setFavoriteNews(newsItem.id, newsItem.isFavorite)
        coVerify { repository.setFavoriteNews(newsItem.id, newsItem.isFavorite) }
    }

    @Test
    fun `should delete a news by id`() = runBlocking {
        val newsItem = NewsMock.news
        coEvery { repository.deleteNewsById(newsItem.id) } returns Unit
        interactor.deleteNewsById(newsItem.id)
        coVerify { repository.deleteNewsById(newsItem.id) }
    }


    @Test(expected = Exception::class)
    fun `should throw an exception when delete a news by id`() = runBlocking {
        val newsItem = NewsMock.news
        coEvery { repository.deleteNewsById(newsItem.id) } throws Exception()
        interactor.deleteNewsById(newsItem.id)
        coVerify { repository.deleteNewsById(newsItem.id) }
    }

    @Test
    fun `should delete all news`() = runBlocking {
        coEvery { repository.deleteAllNews() } returns Unit
        interactor.deleteAllNews()
        coVerify { repository.deleteAllNews() }
    }


    @Test(expected = Exception::class)
    fun `should throw an exception when delete all news`() = runBlocking {
        coEvery { repository.deleteAllNews() } throws Exception()
        interactor.deleteAllNews()
        coVerify { repository.deleteAllNews() }
    }
}