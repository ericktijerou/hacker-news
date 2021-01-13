package com.ericktijerou.hackernews.core

import androidx.annotation.IntDef

@Retention(AnnotationRetention.RUNTIME)
@IntDef(
    ActionType.LOAD,
    ActionType.REFRESH
)

annotation class ActionType {
    companion object {
        const val LOAD = 1
        const val REFRESH = 2
    }
}