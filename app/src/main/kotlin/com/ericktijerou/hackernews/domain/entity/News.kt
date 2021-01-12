package com.ericktijerou.hackernews.domain.entity

data class News(
    val date: String,
    val title: String,
    val storyTitle: String,
    val storyId: Long,
    val author: String,
    val comment: String
)