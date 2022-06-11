package com.pesanbotol.android.app.data.search.model

import com.google.gson.annotations.SerializedName

//
//data class SearchUsersResponse(
//
//    @field:SerializedName("result")
//    val result: Result? = null
//)

data class SearchUsersResponse(

    @field:SerializedName("hits")
    val hits: List<UserItems?>? = null,

//    @field:SerializedName("search_cutoff")
//    val searchCutoff: Boolean? = null,

    @field:SerializedName("found")
    val found: Int? = null,

//    @field:SerializedName("out_of")
//    val outOf: Int? = null,
//
//    @field:SerializedName("request_params")
//    val requestParams: RequestParams? = null,

//    @field:SerializedName("page")
//    val page: Int? = null,

//    @field:SerializedName("facet_counts")
//    val facetCounts: List<Any?>? = null,
//
//    @field:SerializedName("search_time_ms")
//    val searchTimeMs: Int? = null
)


data class UserItems(

//    @field:SerializedName("text_match")
//    val textMatch: Int? = null,
//
//    @field:SerializedName("highlights")
//    val highlights: List<Any?>? = null,

    @field:SerializedName("document")
    val document: UsersDocument? = null
)

data class UsersDocument(

    @field:SerializedName("displayName")
    val displayName: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("username")
    val username: String? = null
)
