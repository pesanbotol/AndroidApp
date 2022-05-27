package com.pesanbotol.android.app.data.auth.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val uid : String?,
    val name : String?,
    val photoUrl : String?,
    val email : String?,
) : Parcelable