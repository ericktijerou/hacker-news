package com.ericktijerou.hackernews.core

import androidx.annotation.IntDef

@Retention(AnnotationRetention.RUNTIME)
@IntDef(
    LoadingState.NONE,
    LoadingState.LOADED,
    LoadingState.LOADING,
    LoadingState.INITIAL_LOADING,
    LoadingState.INITIAL_LOADED,
    LoadingState.REFRESH_LOADING,
    LoadingState.REFRESH_LOADED
)

annotation class LoadingState {
    companion object {
        const val NONE = 0
        const val LOADED = 1
        const val LOADING = 2
        const val INITIAL_LOADING= 4
        const val INITIAL_LOADED = 5
        const val REFRESH_LOADING= 6
        const val REFRESH_LOADED = 7
    }
}