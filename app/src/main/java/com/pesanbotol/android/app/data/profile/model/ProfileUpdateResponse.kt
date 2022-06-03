package com.pesanbotol.android.app.data.bottle.model

import com.google.gson.annotations.SerializedName

data class ProfileUpdateResponse(

    @field:SerializedName("data")
    val data: ProfileUpdate? = null
)

//
//data class RegisteredAt(
//
//	@field:SerializedName("_nanoseconds")
//	val nanoseconds: Int? = null,
//
//	@field:SerializedName("_seconds")
//	val seconds: Int? = null
//)
//
data class ProfileUpdate(

    @field:SerializedName("meta")
    val meta: Meta? = null,

    @field:SerializedName("registeredAt")
    val registeredAt: RegisteredAt? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("username")
    val username: Long? = null
)

data class Meta(

    @field:SerializedName("aggregator")
    val aggregator: Aggregator? = null,

    @field:SerializedName("socials")
    val socials: Any? = null
)

data class Aggregator(

    @field:SerializedName("postCount")
    val postCount: Int? = null,

    @field:SerializedName("likeCount")
    val likeCount: Int? = null,

    @field:SerializedName("recvCommentCount")
    val recvCommentCount: Int? = null,

    @field:SerializedName("recvLikeCount")
    val recvLikeCount: Int? = null,

    @field:SerializedName("commentCount")
    val commentCount: Int? = null
)
