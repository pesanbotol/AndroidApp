package com.pesanbotol.android.app.ui.landing.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.auth.viewmodel.AuthViewModel
import com.pesanbotol.android.app.data.bottle.model.ProfileUpdateResponse
import com.pesanbotol.android.app.data.profile.viewmodel.ProfileViewModel
import com.pesanbotol.android.app.databinding.FragmentAccountBinding
import com.pesanbotol.android.app.ui.edit_profile.EditProfileActivity
import com.pesanbotol.android.app.ui.login.LoginActivity
import com.pesanbotol.android.app.utility.CommonFunction
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AccountFragment : Fragment(), View.OnClickListener {
    private val authViewModel by viewModel<AuthViewModel>()
    private val profileViewModel by viewModel<ProfileViewModel>()
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding
    private var profileResponse: ProfileUpdateResponse? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var myLocation: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding?.tvEditProfile?.setOnClickListener(this)

        _binding?.let { view ->
            authViewModel.firebaseUser()?.let {
                Glide.with(view.root).load(it.photoUrl).apply(
                    RequestOptions().error(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.empty_profile,
                        ),
                    )
                ).into(view.ivPhotoProfile)

            }

        }

        _binding?.btnLogout?.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Keluar")
                .setMessage("Apakah kamu yakin mau keluar?")
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Okay") { dialog, which ->
                    dialog.dismiss()
                    authViewModel.loggedOut()
                }.show()
        }

        authViewModel.authStatusState.observe(viewLifecycleOwner) {
            if (it == false) {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        binding?.refreshSwiper?.setOnRefreshListener {
            binding?.refreshSwiper?.isRefreshing = false
            loadData()
        }
        loadData()

        return binding?.root

    }

    private fun loadData() {
        getMyLocation()
        callProfileViewModel()
    }

    private val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    getMyLocation()
                }
            }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                myLocation = location

                val latLng = LatLng(location.latitude, location.longitude)
                latLng.let {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val addresses: List<Address> =
                            geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    val address: String = addresses[0].getAddressLine(0)
                    val city: String = addresses[0].locality
                    val state: String = addresses[0].adminArea
                    val zip: String = addresses[0].postalCode
                    val country: String = addresses[0].countryName
                    binding?.tvUserLocation?.text = "$city, $state - $country"
                }
//                if (it.geo?.get(0) != null && it.geo[1] != null) {
//                    it.geo.let { geo ->
//
//                    }
//                }
//                binding?.tvUserLocation?.text = "Current Location : (${location.latitude},${
//                    location.longitude
//                })"
            }
        }

    }

    private fun callProfileViewModel() {
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.profileLayout?.visibility = View.GONE
        profileViewModel.getMyProfile()
            .addOnSuccessListener {
                profileResponse = it
                binding?.progressBar?.visibility = View.GONE
                binding?.profileLayout?.visibility = View.VISIBLE
                binding?.apply {
                    tvUserName.text = it?.data?.displayName ?: it?.data?.username?.toString() ?: "-"
                    tvUserBio.text = it?.data?.description ?: "Your bio is empty"
                    binding?.tvSocmedFbId?.text = it?.data?.meta?.socials?.facebook ?: "-"
                    binding?.tvSocmedIgId?.text = it?.data?.meta?.socials?.instagram ?: "-"
                    binding?.tvSocmedTwitterId?.text = it?.data?.meta?.socials?.twitter ?: "-"
                }

            }.addOnFailureListener {
                binding?.progressBar?.visibility = View.GONE
                binding?.profileLayout?.visibility = View.VISIBLE
                CommonFunction.showSnackBar(
                    binding?.root!!,
                    requireContext(),
                    "Gagal mengambil data : ${it.message}",
                    true
                    //                    getString(R.string.file_failed_to_convert),
                )
            }
    }


    override fun onClick(view: View?) {
        profileResponse?.data?.let {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            intent.putExtra(MY_PROFILE, it)
            startActivity(intent)
        }
    }


    companion object {
        var MY_PROFILE: String = "my_profile_data"
    }

}