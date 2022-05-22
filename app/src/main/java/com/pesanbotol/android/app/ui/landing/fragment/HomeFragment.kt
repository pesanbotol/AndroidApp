package com.pesanbotol.android.app.ui.landing.fragment

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.auth.model.User
import com.pesanbotol.android.app.data.bottle.model.BottleItem
import com.pesanbotol.android.app.data.bottle.viewmodel.BottleViewModel
import com.pesanbotol.android.app.data.core.model.BottleCustomMarker
import com.pesanbotol.android.app.databinding.FragmentHomeBinding
import com.pesanbotol.android.app.ui.add_message.AddMessageActivity
import com.pesanbotol.android.app.utility.CommonFunction
import com.pesanbotol.android.app.utility.CustomMarkerRenderer
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment(), OnMapReadyCallback,
    ClusterManager.OnClusterClickListener<BottleCustomMarker>,
    ClusterManager.OnClusterInfoWindowClickListener<BottleCustomMarker>,
    ClusterManager.OnClusterItemClickListener<BottleCustomMarker>,
    ClusterManager.OnClusterItemInfoWindowClickListener<BottleCustomMarker> {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val bottleViewModel by viewModel<BottleViewModel>()
    private lateinit var mMap: GoogleMap
    private var myLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var bounds: ArrayList<LatLng> = arrayListOf()
    private var boundsBuilder = LatLngBounds.Builder()
    private var mClusterManager: ClusterManager<BottleCustomMarker>? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val SDK_INT = Build.VERSION.SDK_INT
        if (SDK_INT > 8) {
            val policy = ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
            //your codes here
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding?.fabAddMessage?.setOnClickListener {
            val intent = Intent(requireActivity(), AddMessageActivity::class.java)
            startActivity(intent)
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        myLocation?.let {
            getBottleContents(LatLng(it.latitude, it.longitude))
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        CommonFunction.setMapStyle(mMap, requireContext())
        setupCluster()
        mMap.setOnMyLocationClickListener {
            myLocation = it
            val latLng = LatLng(it.latitude, it.longitude)
//                mMap.addMarker(
//                    MarkerOptions().position(latLng)
//                )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f))
        }
        getMyLocation()
    }

    private fun setupCluster() {
        mClusterManager = ClusterManager(requireActivity(), mMap)
        mClusterManager?.let {
            mMap.setOnCameraIdleListener(mClusterManager)
            mMap.setOnMarkerClickListener(mClusterManager)
            mMap.setOnInfoWindowClickListener(mClusterManager)
            it.renderer = CustomMarkerRenderer(mMap, requireActivity(), it)
            mClusterManager!!.setOnClusterClickListener(this)
            mClusterManager!!.setOnClusterInfoWindowClickListener(this)
            mClusterManager!!.setOnClusterItemClickListener(this)
            mClusterManager!!.setOnClusterItemInfoWindowClickListener(this)

        }
    }

    private val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    getMyLocation()
                }
            }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    myLocation = it
                    val latLng = LatLng(it.latitude, it.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
//                    mMap.addMarker(MarkerOptions().position(latLng))
                    getBottleContents(latLng)

                }
            }
        } else {
            requestPermissionLauncher.launch(permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getBottleContents(latLng: LatLng) {
        binding?.loadingIndicator?.visibility = View.VISIBLE
        mMap.clear()
        bottleViewModel.getBottle(latLng)
            .addOnSuccessListener {
                setupCluster()
                it?.data?.bottle?.forEach { data ->
                    val latLng = LatLng(
                        data?.geo?.get(0)!!,
                        data.geo[1]!!
                    )
                    mClusterManager?.addItem(
                        BottleCustomMarker(
                            User(
                                "12345",
                                "Arby Azra",
                                "https://api.duniagames.co.id/api/content/upload/file/15157218231625823751.jpg",
                                "arby@gmail.com"
                            ), latLng,
                            data
                        )
                    )
//                    marker?.hideInfoWindow()
                    bounds.add(latLng)
                    boundsBuilder.include(latLng)

//                    mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(requireContext()))

                }.apply {
                    binding?.loadingIndicator?.visibility = View.GONE
                    mClusterManager?.cluster()
                    val bounds: LatLngBounds = boundsBuilder.build()
                    mMap.setOnMapLoadedCallback {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 64))
                    }
                }
            }
            .addOnFailureListener {
                binding?.loadingIndicator?.visibility = View.GONE
                println("Gagal mengambil data : $it")
                binding?.let { binding ->
                    CommonFunction.showSnackBar(
                        binding.root,
                        requireContext(),
                        "Gagal mengambil data : $it",
                        true
                    )
                }

            }
    }

    override fun onClusterClick(cluster: Cluster<BottleCustomMarker>?): Boolean {
        val firstName: String = cluster!!.items.iterator().next()?.user?.name ?: ""
        Toast.makeText(
            activity?.applicationContext,
            cluster.size.toString() + " (including " + firstName + ")",
            Toast.LENGTH_SHORT
        ).show()

        val builder = LatLngBounds.builder()
        for (item in cluster.items) {
            builder.include(item.position)
        }
        val bounds = builder.build()
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }

    override fun onClusterInfoWindowClick(cluster: Cluster<BottleCustomMarker>?) {
        CommonFunction.showSnackBar(
            binding!!.root,
            requireContext(),
            "Todo",
        )
    }

    override fun onClusterItemClick(item: BottleCustomMarker?): Boolean {
        CommonFunction.showSnackBar(
            binding!!.root,
            requireContext(),
            "Todo",
        )
        return true
    }

    override fun onClusterItemInfoWindowClick(item: BottleCustomMarker?) {
        CommonFunction.showSnackBar(
            binding!!.root,
            requireContext(),
            "Todo",
        )
    }


}