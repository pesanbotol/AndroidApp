package com.pesanbotol.android.app.utility

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.pesanbotol.android.app.R

class CustomInfoWindowForGoogleMap(val context: Context) : GoogleMap.InfoWindowAdapter {
    var mContext = context
    private var mWindow: View? =
        (context as Activity).layoutInflater.inflate(R.layout.item_dialog_box_marker_maps, null)

    private fun renderWindowText(marker: Marker, view: View) {

        val tvTitle = view.findViewById<TextView>(R.id.title_username)
        val tvSnippet = view.findViewById<TextView>(R.id.snippet_desc)

        tvTitle.text = marker.title
        tvSnippet.text = marker.snippet
    }

    override fun getInfoContents(marker: Marker): View? {
        renderWindowText(marker, mWindow!!)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        renderWindowText(marker, mWindow!!)
        return mWindow
    }


    fun createDrawableFromView(context: Context, view: View): Bitmap? {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        view.layoutParams = RelativeLayout.LayoutParams(-2, -2)
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.buildDrawingCache()
        val bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}