package com.pesanbotol.android.app.ui.landing.fragment

import android.Manifest.permission
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.bottle.model.BottleContentResponse
import com.pesanbotol.android.app.data.bottle.model.BottleItem
import com.pesanbotol.android.app.data.bottle.model.MissionsItem
import com.pesanbotol.android.app.data.bottle.viewmodel.BottleViewModel
import com.pesanbotol.android.app.data.core.model.BottleCustomMarker
import com.pesanbotol.android.app.data.core.model.TypeCustomMarker
import com.pesanbotol.android.app.data.search.model.BottleDocument
import com.pesanbotol.android.app.data.search.model.MissionsDocument
import com.pesanbotol.android.app.databinding.FragmentHomeBinding
import com.pesanbotol.android.app.ui.add_message.AddMessageActivity
import com.pesanbotol.android.app.ui.detail_bubble.DetailBubbleMessageActivity
import com.pesanbotol.android.app.ui.landing.`interface`.MissionItemClickListener
import com.pesanbotol.android.app.ui.landing.`interface`.SamePlaceItemClickListener
import com.pesanbotol.android.app.ui.landing.adapters.BottleCustomAdapter
import com.pesanbotol.android.app.ui.search.LandingSearchActivity
import com.pesanbotol.android.app.ui.search.fragments.BottleSearchFragment
import com.pesanbotol.android.app.ui.search.fragments.MissionSearchFragment
import com.pesanbotol.android.app.utility.CommonFunction
import com.pesanbotol.android.app.utility.CustomMarkerRenderer
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


class HomeFragment : Fragment(), OnMapReadyCallback,
    ClusterManager.OnClusterClickListener<BottleCustomMarker>,
    ClusterManager.OnClusterInfoWindowClickListener<BottleCustomMarker>,
    ClusterManager.OnClusterItemClickListener<BottleCustomMarker>,
    ClusterManager.OnClusterItemInfoWindowClickListener<BottleCustomMarker> {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val bottleViewModel by sharedViewModel<BottleViewModel>()
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
            resultAddBottleIntent.launch(intent)
        }
        binding?.cardView?.setOnClickListener {
            val intent = Intent(requireActivity(), LandingSearchActivity::class.java)
            getResult.launch(intent)
        }

        return binding?.root
    }

    private val getResult =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    it.data?.extras?.let { bundle ->
                        println("should perform navigation")
                        bundle.getParcelable<BottleDocument>(BottleSearchFragment.BOTTLE_ITEM)
                            ?.let { item ->
                                println("should perform navigation from clicked bottle message")
                                println("should show data ${Gson().toJson(item)}")
                                item.geo?.let { latlng ->
                                    println(
                                        "latlng value ${latlng[0]!!} ${latlng[1]!!}"
                                    )
                                    mMap.animateCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            LatLng(
                                                latlng[0]!!,
                                                latlng[1]!!
                                            ), 13f
                                        )
                                    )
                                }
                            }
                        bundle.getParcelable<MissionsDocument>(MissionSearchFragment.MISSION_ITEM)
                            ?.let { item ->
                                println("should perform navigation from clicked mission")
                                println("should show data ${Gson().toJson(item)}")
                                item.center?.let { latlng ->
                                    println(
                                        "latlng value ${latlng[0]!!} ${latlng[1]!!}"
                                    )
                                    mMap.animateCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            LatLng(
                                                latlng[0]!!,
                                                latlng[1]!!
                                            ), 13f
                                        )
                                    )
                                }
                            }
                    }
                }
            }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        mMap.setOnCameraMoveListener {
            println("mMap.cameraPosition.zoom ${mMap.cameraPosition.zoom}")
        }
        binding?.refresh?.setOnClickListener {
            myLocation?.let {
                getBottleContents(LatLng(it.latitude, it.longitude))
            }

        }
        binding?.myLocation?.setOnClickListener {
            getMyLocation(true)
        }
        mMap.setOnMyLocationClickListener {
            myLocation = it
            val latLng = LatLng(it.latitude, it.longitude)
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

    private fun getMyLocation(isMyLocationButton: Boolean = false) {
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
                    if (isMyLocationButton) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
                    } else {
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                        getBottleContents(latLng)
                    }


                }
            }
        } else {
            requestPermissionLauncher.launch(permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun changeMyLocationPermission(enabled: Boolean) {
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = enabled
        }
    }

    private fun getBottleContents(latLng: LatLng) {
        showLoading()
        mMap.clear()
        bottleViewModel.getBottle(latLng)
            .addOnSuccessListener {
                setupCluster()
                populateMissions(it)
                populateBottles(it)
                mClusterManager?.cluster()
                myLocation?.let { loc ->
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                loc.latitude,
                                loc.longitude
                            ), 13.0f
                        )
                    )
                }
                hideLoading()
            }
            .addOnFailureListener {
                hideLoading()
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

    private fun populateMissions(
        it: BottleContentResponse?,
    ) {
        println(" it?.data?.missions ${it?.data?.missions?.get(0)?.reward}}")
        it?.data?.missions?.forEach { data ->
            val latLng = LatLng(
                data?.center?.get(0)!!,
                data.center.get(1)!!
            )
            mClusterManager?.addItem(
                BottleCustomMarker(
                    TypeCustomMarker.mission, latLng,
                    missionItem = data,
                )
            )
            bounds.add(latLng)
            boundsBuilder.include(latLng)
        }
    }

    private fun populateBottles(
        it: BottleContentResponse?,
    ) {
        it?.data?.bottle?.forEach { data ->
            val latLng = LatLng(
                data?.geo?.get(0)!!,
                data.geo[1]!!
            )
            mClusterManager?.addItem(
                BottleCustomMarker(
                    TypeCustomMarker.bottle, latLng,
                    bottleItem = data,
                )
            )
            bounds.add(latLng)
            boundsBuilder.include(latLng)
        }
    }

    private fun showLoading() {
        binding?.loadingIndicator?.visibility = View.VISIBLE
        binding?.refresh?.visibility = View.GONE
        binding?.myLocation?.visibility = View.GONE
        changeMyLocationPermission(false)
    }

    private fun hideLoading() {
        binding?.loadingIndicator?.visibility = View.GONE
        binding?.refresh?.visibility = View.VISIBLE
        binding?.myLocation?.visibility = View.VISIBLE
        changeMyLocationPermission(true)
    }

    override fun onClusterClick(cluster: Cluster<BottleCustomMarker>?): Boolean {
        val isAllClusterInSamePlace =
                cluster?.items?.reduceOrNull { acc, bottleCustomMarker -> if (acc?.position?.latitude == bottleCustomMarker?.position?.latitude && acc?.position?.longitude == bottleCustomMarker?.position?.longitude) acc else null }
        if (isAllClusterInSamePlace != null) {
            val dialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.bottom_sheet_same_place_dialog, null)
            val rv = view.findViewById<RecyclerView>(R.id.recycler_view)
            val bottlesSize = view.findViewById<TextView>(R.id.show_n_bottles)
            val bottlesLocation = view.findViewById<TextView>(R.id.location)
            val data =
                    cluster.items.map { bottleCustomMarker -> bottleCustomMarker }
            bottlesSize.text =
                    "Menampilkan ${data.size} item"
            (isAllClusterInSamePlace?.missionItem?.center
                ?: isAllClusterInSamePlace.bottleItem?.geo).let {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses: List<Address> =
                        geocoder.getFromLocation(it!![0]!!, it[1]!!, 1)
                val city: String = addresses[0].locality ?: ""
                val state: String = addresses[0].adminArea ?: ""
                val country: String = addresses[0].countryName ?: ""
                val location = "$city, $state - $country"
                bottlesLocation.text = location
            }
            dialog.setContentView(view)
            rv.layoutManager = LinearLayoutManager(requireContext())

            dialog.show()
            rv.adapter = BottleCustomAdapter(
                ArrayList(data),
                object : MissionItemClickListener {
                    override fun onClick(missionsItem: MissionsItem) {
                        val distance = FloatArray(1)
                        myLocation?.let {
                            Location.distanceBetween(

                                it.latitude,
                                it.longitude,
                                missionsItem.center!![0]!!,
                                missionsItem.center[1]!!,
                                distance
                            )
//                            if (distance[0] > 10) {
//                                CommonFunction.showSnackBar(
//                                    dialog.window!!.decorView,
//                                    view.context,
//                                    "Kamu diluar radius 10 meter dari lokasi",
//                                    true
//                                )
//                            } else {
                            submitMission(dialog, view, missionsItem)
//                            }

                        }
                    }

                },
                object : SamePlaceItemClickListener {
                    override fun onClick(bottleItem: BottleItem) {
                        val intent =
                                Intent(requireContext(), DetailBubbleMessageActivity::class.java)
                        intent.putExtra("bubble", bottleItem)
                        startActivity(intent)
                    }

                }
            )
            return true
        } else {
            cluster?.let {
                Toast.makeText(
                    activity?.applicationContext,
                    "Ada " + cluster.size.toString() + " pesan di sini",
                    Toast.LENGTH_SHORT
                ).show()

                val builder = LatLngBounds.builder()
                for (item in it.items) {
                    builder.include(item.position)
                }
                val bounds = builder.build()
                try {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


            return true
        }
    }

    override fun onClusterInfoWindowClick(cluster: Cluster<BottleCustomMarker>?) {
        CommonFunction.showSnackBar(
            binding!!.root,
            requireContext(),
            "Todo",
        )
    }

    override fun onClusterItemClick(item: BottleCustomMarker?): Boolean {
        item?.position?.let {
            if (mMap.cameraPosition.zoom < 13) {
                Toast.makeText(
                    requireContext(),
                    "Tap lagi untuk melihat detail",
                    Toast.LENGTH_SHORT
                ).show()
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(item.position, 13.0f))

            } else {
                if (item.type == TypeCustomMarker.mission) {

                    val dialog = BottomSheetDialog(requireContext())
                    val view = layoutInflater.inflate(R.layout.bottom_sheet_same_place_dialog, null)
                    val rv = view.findViewById<RecyclerView>(R.id.recycler_view)
                    val bottlesSize = view.findViewById<TextView>(R.id.show_n_bottles)
                    val bottlesLocation = view.findViewById<TextView>(R.id.location)
                    bottlesSize.text =
                            "Menampilkan ${item.missionItem?.description}"
                    item.missionItem?.center.let {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val addresses: List<Address> =
                                geocoder.getFromLocation(it!![0]!!, it[1]!!, 1)
                        val city: String = addresses[0].locality ?: ""
                        val state: String = addresses[0].adminArea ?: ""
                        val country: String = addresses[0].countryName ?: ""
                        val location = "$city, $state - $country"
                        bottlesLocation.text = location
                    }
                    dialog.setContentView(view)
                    rv.layoutManager = LinearLayoutManager(requireContext())

                    dialog.show()
                    rv.adapter = BottleCustomAdapter(
                        arrayListOf(item),
                        object : MissionItemClickListener {
                            override fun onClick(missionsItem: MissionsItem) {
                                val distance = FloatArray(1)
                                myLocation?.let {
                                    Location.distanceBetween(

                                        it.latitude,
                                        it.longitude,
                                        missionsItem.center!![0]!!,
                                        missionsItem.center[1]!!,
                                        distance
                                    )
//                                    if (distance[0] > 10) {
//                                        CommonFunction.showSnackBar(
//                                            dialog.window!!.decorView,
//                                            view.context,
//                                            "Kamu diluar radius 10 meter dari lokasi",
//                                            true
//                                        )
//                                    } else {
                                    submitMission(dialog, view, missionsItem)

//                                    }

                                }
                            }

                        },
                        object : SamePlaceItemClickListener {
                            override fun onClick(bottleItem: BottleItem) {
                                val intent =
                                        Intent(
                                            requireContext(),
                                            DetailBubbleMessageActivity::class.java
                                        )
                                intent.putExtra("bubble", bottleItem)
                                startActivity(intent)
                            }

                        }
                    )
                    return true
                }
                val intent = Intent(requireContext(), DetailBubbleMessageActivity::class.java)
                intent.putExtra("bubble", item.bottleItem)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.slide_up, R.anim.no_animation)
            }
        }

        return true
    }

    private fun submitMission(
        dialog: BottomSheetDialog,
        view: View,
        mission: MissionsItem
    ) {
        dialog.dismiss()

        myLocation?.let { loc ->
            if (mission.missionId == null) return
            binding?.overlayIndicator?.visibility = View.VISIBLE
            try {
                bottleViewModel.submitMission(
                    LatLng(
                        loc.latitude,
                        loc.longitude
                    ),
                    mission.missionId
                )
                    .addOnSuccessListener {
                        binding?.overlayIndicator?.visibility = View.GONE
                        CommonFunction.showSnackBar(
                            requireView(),
                            view.context,
                            "Misi telah selesai",
                        )
                    }.addOnFailureListener {
                        binding?.overlayIndicator?.visibility = View.GONE
                        CommonFunction.showSnackBar(
                            requireView(),
                            view.context,
                            "Gagal : ${it.message}",
                            true
                        )
                    }
            } catch (e: Exception) {
                binding?.overlayIndicator?.visibility = View.GONE
                CommonFunction.showSnackBar(
                    dialog.window!!.decorView,
                    view.context,
                    "Gagal : ${e.message}",
                    true
                )
            }

        }
    }

    private val resultAddBottleIntent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    myLocation?.let { loc ->
                        getBottleContents(LatLng(loc.latitude, loc.longitude))
                    }


                }
            }

    override fun onClusterItemInfoWindowClick(item: BottleCustomMarker?) {
    }


}