package com.ericktijerou.hackernews.data.entity

import com.ericktijerou.hackernews.data.cache.entity.NewsEntity
import com.ericktijerou.hackernews.domain.entity.News

data class NewsModel(
    val date: String,
    val title: String,
    val storyTitle: String,
    val storyId: Long,
    val author: String,
    val comment: String
)

fun NewsModel.toLocal() = NewsEntity(
    date = date,
    title = title,
    storyTitle = storyTitle,
    storyId = storyId,
    author = author,
    comment = comment
)

fun NewsModel.toDomain() = News(
    date = date,
    title = title,
    storyTitle = storyTitle,
    storyId = storyId,
    author = author,
    comment = comment
)