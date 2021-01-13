package com.ericktijerou.hackernews.core

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Int,
    val msg: String? = null) {
    companion object {
        val LOADED = NetworkState(Status.LOADED)
        val LOADING = NetworkState(Status.LOADING)
        val REFRESH_LOADED= NetworkState(Status.REFRESH_LOADED)
        val INITIAL_LOADED= NetworkState(Status.INITIAL_LOADED)
        val INITIAL_LOADING= NetworkState(Status.INITIAL_LOADING)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}