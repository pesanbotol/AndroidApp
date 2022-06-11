package com.pesanbotol.android.app.data.search.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BottleItems(

//    @field:SerializedName("text_match")
//    val textMatch: Int? = null,

//    @field:SerializedName("highlights")
//    val highlights: List<Any?>? = null,

    @field:SerializedName("document")
    val document: BottleDocument? = null
) : Parcelable

@Parcelize
data class BottleDocument(

    @field:SerializedName("contentImage.mediaThumbnailUrl")
    val contentImageMediaThumbnailUrl: String? = null,

    @field:SerializedName("geo")
    val geo: List<Double?>? = null,

    @field:SerializedName("createdAt")
    val createdAt: Int? = null,

    @field:SerializedName("uid")
    val uid: String? = null,

    @field:SerializedName("contentImageUrl")
    val contentImageUrl: String? = null,

    @field:SerializedName("contentImage.mediaUrl")
    val contentImageMediaUrl: String? = null,

    @field:SerializedName("kind")
    val kind: String? = null,

    @field:SerializedName("contentText")
    val contentText: String? = null,

    @field:SerializedName("contentImage.kind")
    val contentImageKind: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("_contentImagePath")
    val contentImagePath: String? = null
) : Parcelable

data class RequestParams(

    @field:SerializedName("per_page")
    val perPage: Int? = null,

    @field:SerializedName("q")
    val Q: String? = null,

    @field:SerializedName("collection_name")
    val collectionName: String? = null
)

@Parcelize
data class SearchBottlesResponse(

    @field:SerializedName("hits")
    val hits: List<BottleItems?>? = null,

//    @field:SerializedName("search_cutoff")
//    val searchCutoff: Boolean? = null,

    @field:SerializedName("found")
    val found: Int? = null,

//    @field:SerializedName("out_of")
//    val outOf: Int? = null,
//
//    @field:SerializedName("request_params")
//    val requestParams: RequestParams? = null,
//
//    @field:SerializedName("page")
//    val page: Int? = null,
//
//    @field:SerializedName("facet_counts")
//    val facetCounts: List<Any?>? = null,
//
//    @field:SerializedName("search_time_ms")
//    val searchTimeMs: Int? = null
) : Parcelable
