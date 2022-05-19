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
	val kind: String? = null,
	val contentText: String? = null,
	val id: String? = null
) : Parcelable
