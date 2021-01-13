package com.ericktijerou.hackernews.data.entity

import com.ericktijerou.hackernews.data.cache.entity.NewsEntity
import com.ericktijerou.hackernews.domain.entity.News

data class NewsModel(
    val id: String,
    val date: String,
    val title: String,
    val storyTitle: String,
    val author: String,
    val url: String
)

fun NewsModel.toLocal() = NewsEntity(
    id = id,
    date = date,
    title = title,
    storyTitle = storyTitle,
    author = author,
    url = url
)

fun NewsModel.toDomain() = News(
    id = id,
    date = date,
    title = title,
    storyTitle = storyTitle,
    author = author,
    url = url
)