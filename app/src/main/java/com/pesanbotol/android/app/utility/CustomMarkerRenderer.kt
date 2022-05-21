package com.pesanbotol.android.app.utility

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.core.model.BottleCustomMarker
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.min


class CustomMarkerRenderer(
    private val map: GoogleMap,
    private val activity: FragmentActivity,
    private val clusterManager: ClusterManager<BottleCustomMarker>
) : DefaultClusterRenderer<BottleCustomMarker>(activity.applicationContext, map, clusterManager) {
    private val mIconGenerator = IconGenerator(activity.applicationContext)
    private val mClusterIconGenerator = IconGenerator(activity.applicationContext)
    private var mImageView: ImageView? = null
    private var mClusterImageView: ImageView? = null
    private var mDimension = 0

    init {
        val multiProfile: View =
                activity.layoutInflater.inflate(R.layout.multi_profile, null)
        mClusterIconGenerator.setContentView(multiProfile)
        mClusterImageView = multiProfile.findViewById(R.id.image)

        mImageView = ImageView(activity.applicationContext)
        mDimension = 50
        mImageView?.layoutParams = ViewGroup.LayoutParams(mDimension, mDimension)
        val padding = 2
        mImageView?.setPadding(padding, padding, padding, padding)
        mIconGenerator.setContentView(mImageView)
    }

    override fun onBeforeClusterItemRendered(
        bottle: BottleCustomMarker,
        markerOptions: MarkerOptions
    ) {
        markerOptions
            .icon(getItemIcon(bottle))
            .title(bottle.user.name)

    }

    override fun onClusterItemUpdated(bottle: BottleCustomMarker, marker: Marker) {
        getMarkerBitmapFromView(bottle)?.let {
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(it))
        }
        marker.title = bottle.user.name

    }

    private fun getItemIcon(bottle: BottleCustomMarker): BitmapDescriptor? {
        mImageView?.let {
            Glide.with(activity.applicationContext).load(bottle.user.photoUrl).into(it)
        }
        val icon = mIconGenerator.makeIcon()
        val bitmap = getMarkerBitmapFromView(bottle)!!.let {
            BitmapDescriptorFactory.fromBitmap(it)
        }
        return bitmap
    }

    override fun onBeforeClusterRendered(
        cluster: Cluster<BottleCustomMarker>,
        markerOptions: MarkerOptions
    ) {
        markerOptions.icon(getClusterIcon(cluster))
    }

    override fun onClusterUpdated(cluster: Cluster<BottleCustomMarker>, marker: Marker) {
        marker.setIcon(getClusterIcon(cluster))
    }

    private fun getClusterIcon(cluster: Cluster<BottleCustomMarker>): BitmapDescriptor {
        val profilePhotos: MutableList<Drawable> = ArrayList(min(1, cluster.size))
        val width = mDimension
        val height = mDimension
        for (p in cluster.items) {

            val drawable: Drawable = getNetworkImage(p) ?: getEmptyProfileDrawable()
            drawable.setBounds(0, 0, width, height)
            profilePhotos.add(drawable)
        }
        val multiDrawable = MultiDrawable(profilePhotos)
        multiDrawable.setBounds(0, 0, width, height)
        mClusterImageView!!.setImageDrawable(multiDrawable)
        val icon = mClusterIconGenerator.makeIcon(cluster.size.toString())
        return BitmapDescriptorFactory.fromBitmap(icon)
    }

    private fun getEmptyProfileDrawable() = ContextCompat.getDrawable(
        activity.applicationContext,
        R.drawable.empty_profile
    )!!

    private fun getNetworkImage(p: BottleCustomMarker): Drawable? {
        if (p.bottleItem.contentImageUrl == null) {
            return null
        }
        val connection: HttpURLConnection =
                URL(p.bottleItem.contentImageUrl).openConnection() as HttpURLConnection
        connection.setRequestProperty("User-agent", "Mozilla/4.0")

        connection.connect()
        val input: InputStream = connection.inputStream

        return Drawable.createFromStream(input, null)
    }

    override fun shouldRenderAsCluster(cluster: Cluster<BottleCustomMarker>): Boolean {
        return cluster.size > 1
    }


    private fun getMarkerBitmapFromView(bottle: BottleCustomMarker): Bitmap? {
        val bottleImage = getNetworkImage(bottle)
        val customMarkerView: View? =
                (activity.layoutInflater as LayoutInflater?)?.inflate(
                    R.layout.item_dialog_box_marker_maps,
                    null
                )
        val markerImageView =
                customMarkerView?.findViewById<View>(R.id.image_dialog_profile) as ImageView
        val markerUsernameView =
                customMarkerView.findViewById<View>(R.id.title_username) as TextView
        val markerDescView =
                customMarkerView.findViewById<View>(R.id.snippet_desc) as TextView
        val markerTimeView =
                customMarkerView.findViewById<View>(R.id.tv_time_upload) as TextView
        markerUsernameView.text = bottle.user.name
        markerDescView.text = bottle.snippet ?: ""
        markerTimeView.text = "unknown"
        if (bottleImage == null) {
            markerImageView.visibility = View.GONE
        } else {
            markerImageView.setImageDrawable(bottleImage)

        }
        markerImageView.background =
                ContextCompat.getDrawable(
                    activity.applicationContext,
                    R.drawable.rounded_outline
                )
        markerImageView.clipToOutline = true
//        markerImageView.setImageDrawable(getEmptyProfileDrawable())
//        markerImageView.background =
//                ContextCompat.getDrawable(
//                    activity.applicationContext,
//                    R.drawable.rounded_outline
//                )
//        markerImageView.clipToOutline = true


        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(
            0,
            0,
            customMarkerView.measuredWidth,
            customMarkerView.measuredHeight
        )
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView.measuredWidth, customMarkerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.background
        drawable?.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }




}