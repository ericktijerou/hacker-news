package com.ericktijerou.hackernews.data

import com.ericktijerou.hackernews.data.cache.NewsDataStore
import com.ericktijerou.hackernews.data.network.NewsCloudStore
import com.ericktijerou.hackernews.data.repository.NewsRepositoryImp
import com.ericktijerou.hackernews.mock.NewsMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test

class NewsRepositoryTest {

    private val dataStore = mockk<NewsDataStore>()
    private val cloudStore = mockk<NewsCloudStore>()
    private val userRepository = NewsRepositoryImp(cloudStore, dataStore)

    @Test
    fun `should set a favorites by id`() = runBlocking {
        val newsItem = NewsMock.news
        coEvery { dataStore.setFavoriteNews(newsItem.id, newsItem.isFavorite) } returns Unit
        userRepository.setFavoriteNews(newsItem.id, newsItem.isFavorite)
        coVerify { dataStore.setFavoriteNews(newsItem.id, newsItem.isFavorite) }
    }


    @Test(expected = Exception::class)
    fun `should throw an exception when set a favorite by id`() = runBlocking {
        val newsItem = NewsMock.news
        coEvery { dataStore.setFavoriteNews(newsItem.id, newsItem.isFavorite) } throws Exception()
        userRepository.setFavoriteNews(newsItem.id, newsItem.isFavorite)
        coVerify { dataStore.setFavoriteNews(newsItem.id, newsItem.isFavorite) }
    }

    @Test
    fun `should delete a news by id`() = runBlocking {
        val newsItem = NewsMock.news
        coEvery { dataStore.deleteById(newsItem.id) } returns Unit
        userRepository.deleteNewsById(newsItem.id)
        coVerify { dataStore.deleteById(newsItem.id) }
    }


    @Test(expected = Exception::class)
    fun `should throw an exception when delete a news by id`() = runBlocking {
        val newsItem = NewsMock.news
        coEvery { dataStore.deleteById(newsItem.id) } throws Exception()
        userRepository.deleteNewsById(newsItem.id)
        coVerify { dataStore.deleteById(newsItem.id) }
    }

    @Test
    fun `should delete all news`() = runBlocking {
        coEvery { dataStore.deleteAll() } returns Unit
        userRepository.deleteAllNews()
        coVerify { dataStore.deleteAll() }
    }


    @Test(expected = Exception::class)
    fun `should throw an exception when delete all news`() = runBlocking {
        coEvery { dataStore.deleteAll() } throws Exception()
        userRepository.deleteAllNews()
        coVerify { dataStore.deleteAll() }
    }
}