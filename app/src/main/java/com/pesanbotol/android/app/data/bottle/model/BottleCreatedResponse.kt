package com.pesanbotol.android.app.data.bottle.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BottleCreatedResponse(
    val data: BottleCreated? = null
) : Parcelable

@Parcelize
data class BottleCreated(
    val kind: String? = null,
    val contentText: String? = null,
    val flags: List<String?>? = null,
    val autoTags: List<String?>? = null,
    val likeCount: Int? = null,
    val lastLikeAt: String? = null,
    val commentCount: Int? = null,
    val tags: List<String?>? = null,
    val geo: List<String?>? = null,
    val uid: String? = null,
    val lastCommentAt: String? = null,
    val mentions: List<String?>? = null,
    val lastSignalAt: String? = null
) : Parcelable
