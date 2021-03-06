package com.pesanbotol.android.app.utility

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.snackbar.Snackbar
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.utility.Utils.Companion.convertDpToPixel

class CommonFunction {
    companion object {
        val REQUIRED_PERMISSIONS =
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
        const val REQUEST_CODE_PERMISSIONS = 10
        const val LOCATION_PERMISSION_REQUEST_CODE = 1

        fun showSnackBar(
            view: View,
            context: Context,
            message: String,
            error: Boolean? = null
        ) {
            val s = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                .setTextColor(
                    ContextCompat.getColor(
                        context,
                        if (error == true) R.color.pink else R.color.blue_1100
                    )
                )
            val sView = s.view
            sView.translationY = -(convertDpToPixel(50.0f, context))
            s.show()
        }


        fun allPermissionsGranted(context: Context) = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        fun setMapStyle(map: GoogleMap, context: Context) {
            try {
                val success =
                        map.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                context,
                                R.raw.pesanbotol_map_style
                            )
                        )
                if (!success) {
                    Log.e(context.applicationContext.javaClass.simpleName, "Style parsing failed.")
                }
            } catch (exception: Resources.NotFoundException) {
                Log.e(
                    context.applicationContext.javaClass.simpleName,
                    "Can't find style. Error: ",
                    exception
                )
            }
        }

    }
}