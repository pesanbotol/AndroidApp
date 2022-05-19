package com.pesanbotol.android.app.ui.landing.fragment

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.gms.maps.model.MarkerOptions
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.bottle.viewmodel.BottleViewModel
import com.pesanbotol.android.app.databinding.FragmentHomeBinding
import com.pesanbotol.android.app.ui.add_message.AddMessageActivity
import com.pesanbotol.android.app.utility.CommonFunction
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val bottleViewModel by viewModel<BottleViewModel>()
    private lateinit var mMap: GoogleMap
    private var myLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var bounds: ArrayList<LatLng> = arrayListOf()
    private var boundsBuilder = LatLngBounds.Builder()

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

    override fun onStop() {
        super.onStop()
        myLocation?.let {
            getBottleContents(LatLng(it.latitude, it.longitude))
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.setOnCameraMoveListener {
//            val bounds = mMap.projection.visibleRegion.latLngBounds
//            println("Bounds Center : lat ${bounds.center.latitude}, lng ${bounds.center.longitude}")
//            println("Bounds northeast :lat ${bounds.northeast.latitude}, lng ${bounds.northeast.longitude}")
//            println("Bounds southwest :lat ${bounds.southwest.latitude}, lng ${bounds.southwest.longitude}")
        }
        mMap.setOnMyLocationClickListener {
            mMap.setOnMyLocationClickListener {
                myLocation = it
                val latLng = LatLng(it.latitude, it.longitude)
//                mMap.addMarker(
//                    MarkerOptions().position(latLng)
//                )
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                getBottleContents(latLng)
            }
        }


        getMyLocation()
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
        bottleViewModel.getBottle(latLng)
            .addOnSuccessListener {
                it?.data?.bottle?.forEach { data ->
                    val latLng = LatLng(
                        data!!.geo?.get(0)!!,
                        data.geo?.get(1)!!
                    )
                    mMap.addMarker(
                        MarkerOptions().position(
                            latLng
                        )

                    )
                    bounds.add(latLng)
                    boundsBuilder.include(latLng)
                }.apply {

                }
                val bounds: LatLngBounds = boundsBuilder.build()
                mMap.setOnMapLoadedCallback {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 64))
                }
            }
            .addOnFailureListener {
                println("Gagal mengambil data : $it")
                CommonFunction.showSnackBar(
                    binding!!.root,
                    requireContext(),
                    "Gagal mengambil data : $it",
                    true
                )
            }
    }

}