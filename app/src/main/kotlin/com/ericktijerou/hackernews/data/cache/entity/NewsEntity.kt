package com.ericktijerou.hackernews.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ericktijerou.hackernews.data.entity.NewsModel
import com.ericktijerou.hackernews.domain.entity.News

@Entity(tableName = "News")
data class NewsEntity(
    @PrimaryKey var id: String,
    val date: String,
    val title: String,
    val storyTitle: String,
    val author: String,
    val url: String,
    val isFavorite: Boolean
)

fun NewsEntity.toData() = NewsModel(
    id = id,
    date = date,
    title = title,
    storyTitle = storyTitle,
    author = author,
    url = url,
    isFavorite = isFavorite
)