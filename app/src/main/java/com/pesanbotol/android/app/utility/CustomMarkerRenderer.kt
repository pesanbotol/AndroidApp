package com.pesanbotol.android.app.utility

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.core.model.UserCustomMarker
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.min


class CustomMarkerRenderer(
    private val map: GoogleMap,
    private val activity: FragmentActivity,
    private val clusterManager: ClusterManager<UserCustomMarker>
) : DefaultClusterRenderer<UserCustomMarker>(activity.applicationContext, map, clusterManager) {
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

    override fun onBeforeClusterItemRendered(user: UserCustomMarker, markerOptions: MarkerOptions) {
        markerOptions
            .icon(getItemIcon(user))
            .title(user.user.name)
    }

    override fun onClusterItemUpdated(user: UserCustomMarker, marker: Marker) {
        marker.setIcon(getItemIcon(user))
        marker.title = user.user.name
    }

    private fun getItemIcon(user: UserCustomMarker): BitmapDescriptor? {
        mImageView?.let {
            Glide.with(activity.applicationContext).load(user.user.photoUrl).into(it)
        }
        val icon = mIconGenerator.makeIcon()
        return BitmapDescriptorFactory.fromBitmap(icon)
    }

    override fun onBeforeClusterRendered(
        cluster: Cluster<UserCustomMarker>,
        markerOptions: MarkerOptions
    ) {
        markerOptions.icon(getClusterIcon(cluster))
    }

    override fun onClusterUpdated(cluster: Cluster<UserCustomMarker>, marker: Marker) {
        marker.setIcon(getClusterIcon(cluster))
    }

    private fun getClusterIcon(cluster: Cluster<UserCustomMarker>): BitmapDescriptor {
        val profilePhotos: MutableList<Drawable> = ArrayList(min(1, cluster.size))
        val width = mDimension
        val height = mDimension
        for (p in cluster.items) {

            if (profilePhotos.size == 1) break
            val connection: HttpURLConnection =
                    URL(p.user.photoUrl).openConnection() as HttpURLConnection
            connection.setRequestProperty("User-agent", "Mozilla/4.0")

            connection.connect()
            val input: InputStream = connection.inputStream

            val drawable: Drawable = Drawable.createFromStream(input, null)
            drawable.setBounds(0, 0, width, height)
            profilePhotos.add(drawable)
        }
        val multiDrawable = MultiDrawable(profilePhotos)
        multiDrawable.setBounds(0, 0, width, height)
        mClusterImageView!!.setImageDrawable(multiDrawable)
        val icon = mClusterIconGenerator.makeIcon(cluster.getSize().toString())
        return BitmapDescriptorFactory.fromBitmap(icon)
    }

    override fun shouldRenderAsCluster(cluster: Cluster<UserCustomMarker>): Boolean {
        return cluster.size > 1
    }


}