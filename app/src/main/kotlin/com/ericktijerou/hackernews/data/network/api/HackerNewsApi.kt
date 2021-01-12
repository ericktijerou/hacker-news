package com.ericktijerou.hackernews.data.network.api

import com.ericktijerou.hackernews.data.network.entity.BaseResponse
import com.ericktijerou.hackernews.data.network.entity.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HackerNewsApi {
    @GET("api/v1/search_by_date?tags=story&query=android")
    suspend fun getNewsList(
        @Query("page") number: Int,
        @Query("hitsPerPage") perPage: Int
    ): Response<BaseResponse<List<NewsResponse>>>
}