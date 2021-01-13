package com.ericktijerou.hackernews.mock

import com.ericktijerou.hackernews.domain.entity.News

object NewsMock {
    val news: News get() = News(
        id = "00000",
        date = "yyyy-MM-dd'T'HH:mm:ss.SSSX",
        title = "Title",
        storyTitle = "Story Title",
        author = "Jhon Rambo",
        url = "www.google.com",
        isFavorite = true
    )
}