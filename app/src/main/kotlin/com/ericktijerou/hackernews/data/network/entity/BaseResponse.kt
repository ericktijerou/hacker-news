package com.ericktijerou.hackernews.data.network.entity

import com.google.gson.annotations.SerializedName

open class BaseResponse<T> {
    @SerializedName("hits")
    var hits: T? = null

    @SerializedName("nbHits")
    var nbHits: Int? = null

    @SerializedName("page")
    var page: Int? = null

    @SerializedName("nbPages")
    var nbPages: Int? = null

    @SerializedName("hitsPerPage")
    var hitsPerPage: Int? = null

    @SerializedName("exhaustiveNbHits")
    var exhaustiveNbHits: Boolean? = null

    @SerializedName("query")
    var query: String? = null

    @SerializedName("params")
    var params: String? = null

    @SerializedName("processingTimeMS")
    var processingTimeMS: Long? = null
}

