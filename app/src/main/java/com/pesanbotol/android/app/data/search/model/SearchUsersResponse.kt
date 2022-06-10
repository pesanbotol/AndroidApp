package com.pesanbotol.android.app.data.search.model

import com.google.gson.annotations.SerializedName

data class SearchUsersResponse(

    @field:SerializedName("result")
    val result: Result? = null
)

data class UsersDocument(

    @field:SerializedName("displayName")
    val displayName: String? = null,

    @field:SerializedName("description")
    override val description: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("username")
    val username: String? = null
) : Document()
