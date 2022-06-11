package com.pesanbotol.android.app.data.core.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.pesanbotol.android.app.data.auth.model.User
import com.pesanbotol.android.app.data.bottle.model.BottleItem
import com.pesanbotol.android.app.data.bottle.model.MissionsItem

enum class TypeCustomMarker { bottle, mission, event }
class BottleCustomMarker(
    val type: TypeCustomMarker,
    private val latLng: LatLng,
    val bottleItem: BottleItem? = null,
    val missionItem: MissionsItem? = null
) :
    ClusterItem {
    override fun getPosition(): LatLng {
        return latLng
    }

    override fun getTitle(): String {
        return when (type) {
            TypeCustomMarker.bottle -> bottleItem?.user?.displayName ?: ""
            else -> if (missionItem?.description != null) "Misi : ${missionItem.description}" else ""
        }
    }

    override fun getSnippet(): String {
        return when (type) {
            TypeCustomMarker.bottle -> bottleItem?.contentText ?: ""
            else -> if (missionItem?.reward != null) "Reward : ${missionItem.reward}" else ""
        }
    }
}