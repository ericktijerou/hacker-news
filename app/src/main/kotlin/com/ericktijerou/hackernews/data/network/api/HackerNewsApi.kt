package com.ericktijerou.hackernews.data.network.api

import com.ericktijerou.hackernews.data.network.entity.BaseResponse
import com.ericktijerou.hackernews.data.network.entity.NewsResponse
import retrofit2.Response
import retrofit2.http.GET

interface HackerNewsApi {

    @GET("api/v1/search_by_date?query=android")
    suspend fun getNewsList(): Response<BaseResponse<List<NewsResponse>>>

}