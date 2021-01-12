package com.ericktijerou.hackernews.data.network.entity

import com.ericktijerou.hackernews.core.orZero
import com.ericktijerou.hackernews.data.entity.NewsModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    @SerialName("created_at")
    val date: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("story_title")
    val storyTitle: String? = null,
    @SerialName("story_id")
    val storyId: Long? = null,
    @SerialName("author")
    val author: String? = null,
    @SerialName("comment_text")
    val comment: String? = null
)

fun NewsResponse.toData() = NewsModel(
    date = date.orEmpty(),
    title = title.orEmpty(),
    storyTitle = storyTitle.orEmpty(),
    storyId = storyId.orZero(),
    author = author.orEmpty(),
    comment = comment.orEmpty()
)