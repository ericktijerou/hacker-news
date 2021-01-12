package com.ericktijerou.hackernews.core

fun Long?.orZero() : Long = this ?: 0

inline fun <reified T : Any> listByElementsOf(vararg elements: Any): List<T> {
    val mutableList = mutableListOf<T>()
    elements.forEach { element ->
        if (element is T) {
            mutableList += element
        } else if (element is List<*>) {
            mutableList += element.mapNotNull { it as? T }
        }
    }
    return mutableList
}