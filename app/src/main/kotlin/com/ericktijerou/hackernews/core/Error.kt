package com.ericktijerou.hackernews.core

sealed class Error {
    object Network : Error()
    object NotFound : Error()
    object Unknown : Error()
}