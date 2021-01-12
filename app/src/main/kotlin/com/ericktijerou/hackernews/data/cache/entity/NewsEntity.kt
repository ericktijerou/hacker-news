package com.ericktijerou.hackernews.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ericktijerou.hackernews.data.entity.NewsModel

@Entity(tableName = "News")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val date: String,
    val title: String,
    val storyTitle: String,
    val storyId: Long,
    val author: String,
    val comment: String
)

fun NewsEntity.toData() = NewsModel(
    date = date,
    title = title,
    storyTitle = storyTitle,
    storyId = storyId,
    author = author,
    comment = comment
)