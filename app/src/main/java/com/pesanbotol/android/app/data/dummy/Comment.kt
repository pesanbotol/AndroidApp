package com.pesanbotol.android.app.data.dummy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    var avatar: String,
    var name: String,
    var descs: String,
    var date: String,
) : Parcelable
