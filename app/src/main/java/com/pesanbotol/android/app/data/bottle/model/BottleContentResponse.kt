package com.pesanbotol.android.app.data.bottle.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BottleContentResponse(
    val data: Data? = null
) : Parcelable

@Parcelize
data class Data(
//	val trend: List<Any?>? = null,
//	val bottleRecommended: List<Any?>? = null,
    val bottle: List<BottleItem?>? = null
) : Parcelable

@Parcelize
data class BottleItem(
    val geo: List<Double?>? = null,
    val createdAt: Int? = null,
    val uid: String? = null,
    val contentImage: ContentImage? = null,
    val kind: String? = null,
    val contentText: String? = null,
    val id: String? = null,
    val contentImagePath: String? = null,
    val user: User? = null
) : Parcelable


@Parcelize
data class RegisteredAt(
    val nanoseconds: Int? = null,
    val seconds: Int? = null
) : Parcelable

@Parcelize
data class User(
    val registeredAt: RegisteredAt? = null,
    val description: String? = null,
    val username: String? = null,
    val avatar: Avatar? = null,
    val displayName: String? = null,
) : Parcelable


@Parcelize
data class ContentImage(
    val kind: String? = null,
    val mediaUrl: String? = null,
    val mediaThumbnailUrl: String? = null
) : Parcelable
