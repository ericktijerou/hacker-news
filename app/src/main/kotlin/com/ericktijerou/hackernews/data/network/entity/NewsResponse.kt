package com.ericktijerou.hackernews.data.network.entity

import com.ericktijerou.hackernews.data.entity.NewsModel
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("created_at")
    val date: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("story_title")
    val storyTitle: String? = null,
    @SerializedName("objectID")
    val objectId: String? = null,
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("url")
    val url: String? = null
)

fun NewsResponse.toData() = NewsModel(
    id = objectId.orEmpty(),
    date = date.orEmpty(),
    title = title.orEmpty(),
    storyTitle = storyTitle.orEmpty(),
    author = author.orEmpty(),
    url = url.orEmpty()
)