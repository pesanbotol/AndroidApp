package com.pesanbotol.android.app.data.response


data class CreateMessageResponse(
    val contextText: String? = null,
    val geo: List<GeoItem>,
)

data class GeoItem(
    val latitude: Int?,
    val longitude: Int?
)
