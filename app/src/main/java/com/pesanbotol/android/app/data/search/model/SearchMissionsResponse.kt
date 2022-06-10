package com.pesanbotol.android.app.data.search.model

import com.google.gson.annotations.SerializedName

data class SearchMissionsResponse(

	@field:SerializedName("result")
	val result: Result? = null
)

data class MissionsDocument(

	@field:SerializedName("reward")
	val reward: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: Int? = null,

	@field:SerializedName("geofence")
	val geofence: List<Double?>? = null,

	@field:SerializedName("enable")
	val enable: Boolean? = null,

	@field:SerializedName("kind")
	val kind: String? = null,

	@field:SerializedName("center")
	val center: List<Double?>? = null,

	@field:SerializedName("description")
	override val description: String? = null,

	@field:SerializedName("_class_id")
	val classId: Int? = null,

	@field:SerializedName("id")
	val id: String? = null
):Document()
