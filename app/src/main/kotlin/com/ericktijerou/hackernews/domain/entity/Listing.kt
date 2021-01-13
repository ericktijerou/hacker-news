package com.ericktijerou.hackernews.domain.entity

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ericktijerou.hackernews.core.NetworkState

data class Listing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>
)