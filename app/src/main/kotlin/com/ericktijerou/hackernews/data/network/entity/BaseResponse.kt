package com.ericktijerou.hackernews.data.network.entity

open class BaseResponse<T> {
    var hits: T? = null
    var nbHits: Int? = null
    var page: Int? = null
    var nbPages: Int? = null
    var hitsPerPage: Int? = null
    var exhaustiveNbHits: Boolean? = null
    var query: String? = null
    var params: String? = null
    var processingTimeMS: Long? = null
}

