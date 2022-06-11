package com.pesanbotol.android.app.data.bottle.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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
@Parcelize
data class ProfileUpdate(

    @field:SerializedName("meta")
    val meta: Meta? = null,

    @field:SerializedName("registeredAt")
    val registeredAt: RegisteredAt? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("username")
    val username: Long? = null,

    @field:SerializedName("displayName")
    val displayName: String? = null,

    @field:SerializedName("avatar")
    val avatar: Avatar? = null
) : Parcelable

@Parcelize
data class Meta(

    @field:SerializedName("aggregator")
    val aggregator: Aggregator? = null,

    @field:SerializedName("socials")
    val socials: Socials? = null,
    @field:SerializedName("badges")
    val badges: List<String?>? = null,
    @field:SerializedName("missions")
    val missions: List<MissionsItem?>? = null
) : Parcelable

@Parcelize
data class Avatar(
    val kind: String? = null,
    val mediaThumbnailUrl: String? = null,
    val mediaUrl: String? = null
) : Parcelable

@Parcelize
data class Socials(
    @field:SerializedName("facebook")
    val facebook: String? = null,

    @field:SerializedName("twitter")
    val twitter: String? = null,

    @field:SerializedName("instagram")
    val instagram: String? = null
) : Parcelable

@Parcelize
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
) : Parcelable


