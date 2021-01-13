package com.ericktijerou.hackernews.domain.entity

data class News(
    val id: String,
    val date: String,
    val title: String,
    val storyTitle: String,
    val author: String,
    val url: String,
    val isFavorite: Boolean
)