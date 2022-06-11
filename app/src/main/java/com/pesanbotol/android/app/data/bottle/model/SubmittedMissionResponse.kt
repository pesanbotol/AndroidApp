package com.pesanbotol.android.app.data.bottle.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubmittedMissionResponse(
	val result: Result? = null
) : Parcelable

@Parcelize
data class SubmittedAt(
	val nanoseconds: Int? = null,
	val seconds: Int? = null
) : Parcelable

@Parcelize
data class Result(
	val geo: List<Double?>? = null,
	val image: Image? = null,
	val missionId: String? = null,
	val rewarded: List<String?>? = null,
	val imagePath: String? = null,
	val submittedAt: SubmittedAt? = null
) : Parcelable

@Parcelize
data class Image(
	val mediaUrl: String? = null,
	val kind: String? = null,
	val mediaThumbnailUrl: String? = null
) : Parcelable
