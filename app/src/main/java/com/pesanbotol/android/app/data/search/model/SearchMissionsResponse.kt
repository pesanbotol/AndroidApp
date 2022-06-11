package com.pesanbotol.android.app.data.search.model

import com.google.gson.annotations.SerializedName

//data class SearchMissionsResponse(

//	@field:SerializedName("result")
//	val result: Result? = null
//)


data class SearchMissionResponse(

	@field:SerializedName("hits")
	val hits: List<MissionItems?>? = null,

//	@field:SerializedName("search_cutoff")
//	val searchCutoff: Boolean? = null,

	@field:SerializedName("found")
	val found: Int? = null,

//	@field:SerializedName("out_of")
//	val outOf: Int? = null,
//
//	@field:SerializedName("request_params")
//	val requestParams: RequestParams? = null,
//
//	@field:SerializedName("page")
//	val page: Int? = null,
//
//	@field:SerializedName("facet_counts")
//	val facetCounts: List<Any?>? = null,
//
//	@field:SerializedName("search_time_ms")
//	val searchTimeMs: Int? = null
)


data class MissionItems(

//	@field:SerializedName("text_match")
//	val textMatch: Int? = null,
//
//	@field:SerializedName("highlights")
//	val highlights: List<Any?>? = null,

	@field:SerializedName("document")
	val document: MissionsDocument? = null
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
	val description: String? = null,

	@field:SerializedName("_class_id")
	val classId: Int? = null,

	@field:SerializedName("id")
	val id: String? = null
)
