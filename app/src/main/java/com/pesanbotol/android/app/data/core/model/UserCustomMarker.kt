package com.pesanbotol.android.app.data.core.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.pesanbotol.android.app.data.auth.model.User

class UserCustomMarker(val user: User, val latLng: LatLng) : ClusterItem {
    override fun getPosition(): LatLng {
        return latLng
    }

    override fun getTitle(): String? {
        return null
    }

    override fun getSnippet(): String? {
        return null
    }
}