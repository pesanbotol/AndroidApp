package com.pesanbotol.android.app.data.core.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.pesanbotol.android.app.data.auth.model.User
import com.pesanbotol.android.app.data.bottle.model.BottleItem

class BottleCustomMarker(val user: User, private val latLng: LatLng,val bottleItem: BottleItem) : ClusterItem {
    override fun getPosition(): LatLng {
        return latLng
    }

    override fun getTitle(): String? {
        return null
    }

    override fun getSnippet(): String? {
        return bottleItem.contentText
    }
}