package com.ericktijerou.hackernews.core

import androidx.annotation.IntDef

@Retention(AnnotationRetention.RUNTIME)
@IntDef(
    Status.NONE,
    Status.LOADED,
    Status.LOADING,
    Status.INITIAL_LOADING,
    Status.INITIAL_LOADED,
    Status.REFRESH_LOADED,
    Status.FAILED
)

annotation class Status {
    companion object {
        const val NONE = 0
        const val LOADED = 1
        const val LOADING = 2
        const val INITIAL_LOADING= 3
        const val INITIAL_LOADED = 4
        const val REFRESH_LOADED = 5
        const val FAILED = 6
    }
}