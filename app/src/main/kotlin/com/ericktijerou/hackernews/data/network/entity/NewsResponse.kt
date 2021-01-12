package com.ericktijerou.hackernews.data.network.entity

import com.ericktijerou.hackernews.core.orZero
import com.ericktijerou.hackernews.data.entity.NewsModel
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("created_at")
    val date: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("story_title")
    val storyTitle: String? = null,
    @SerializedName("story_id")
    val storyId: Long? = null,
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("comment_text")
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