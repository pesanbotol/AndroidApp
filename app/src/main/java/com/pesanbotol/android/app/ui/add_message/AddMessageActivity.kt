package com.pesanbotol.android.app.ui.add_message

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.auth.viewmodel.AuthViewModel
import com.pesanbotol.android.app.data.bottle.viewmodel.BottleViewModel
import com.pesanbotol.android.app.databinding.ActivityAddMessageBinding
import com.pesanbotol.android.app.utility.*
import com.pesanbotol.android.app.utility.CommonFunction.Companion.REQUEST_CODE_PERMISSIONS
import com.pesanbotol.android.app.utility.CommonFunction.Companion.REQUIRED_PERMISSIONS
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddMessageActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private lateinit var binding: ActivityAddMessageBinding
    private var photo: File? = null
    private lateinit var mMap: GoogleMap
    private var myLocation: Location? = null
    private val bottleViewModel by viewModel<BottleViewModel>()
    private val authViewModel by viewModel<AuthViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapManager =
                supportFragmentManager.findFragmentById(R.id.map_add_story) as SupportMapFragment?
        mapManager?.getMapAsync(this)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.uploadImageLayout.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_upload_image_dialog, null)
            val btnCamera = view.findViewById<CardView>(R.id.btn_camera)
            val btnGallery = view.findViewById<CardView>(R.id.btn_gallery)
            btnCamera.setOnClickListener {
                dialog.dismiss()
                startTakePhoto()
            }
            btnGallery.setOnClickListener {
                dialog.dismiss()
                startGallery()
            }
            dialog.setContentView(view)
            dialog.show()
        }
        binding.btnPost.setOnClickListener(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.apply {
            binding.addMapFrame.background =
                    ContextCompat.getDrawable(this@AddMessageActivity, R.drawable.rounded_outline)
            binding.addMapFrame.clipToOutline = true
            setAllGesturesEnabled(true)
            isScrollGesturesEnabledDuringRotateOrZoom = true
            isZoomControlsEnabled = true
        }
        mMap.setOnMyLocationButtonClickListener {
            mMap.clear()
            getMyLocation()
            true
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
        mMap.isMyLocationEnabled = true
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
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
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f))
                mMap.addMarker(MarkerOptions().position(latLng))
                binding.tvLocation?.text = "Location (${location.latitude},${
                    location.longitude
                })"
            }
        }

    }


    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddMessageActivity,
                "com.pesanbotol.android.app",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            photo = File(currentPhotoPath)

//            val result = BitmapFactory.decodeFile(myFile.path)
            val result = rotateBitmap(
                BitmapFactory.decodeFile(photo!!.path),
                true
            )
            binding.previewImage.setImageBitmap(result)
            binding.previewImage.background =
                    ContextCompat.getDrawable(this, R.drawable.rounded_outline)
            binding.previewImage.clipToOutline = true
            binding.uploadImagePlaceholder.visibility = View.GONE
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            photo = uriToFile(selectedImg, this@AddMessageActivity)
            binding.previewImage.setImageURI(selectedImg)
            binding.previewImage.background =
                    ContextCompat.getDrawable(this@AddMessageActivity, R.drawable.rounded_outline)
            binding.previewImage.clipToOutline = true
            binding.uploadImagePlaceholder.visibility = View.GONE
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnPost.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.btnPost.isEnabled = true
    }

    override fun onClick(p0: View?) {
//        if (photo == null) {
//            CommonFunction.showSnackBar(
//                binding.root,
//                applicationContext,
//                getString(R.string.photo_empty_warning),
//                true
//            )
//            return
//        }
//        photo?.let {
        try {
            if (binding.etDescription.text?.isEmpty() == true) {
                binding.etDescription.error = "Konten tidak boleh kosong"
//                binding.etDescription.error = getString(R.string.description_empty_warning)
                return
            }
            val defaultLocation = LatLng(-5.778581, 109.640097)
            processBottleCreation(defaultLocation)
//            reduceFileImage(it).let { reduced ->
//                createBottle(defaultLocation)
//            }
        } catch (e: Exception) {
            CommonFunction.showSnackBar(
                binding.root,
                applicationContext,
                "Gagal mengunggah gambar",
//                    getString(R.string.file_failed_to_convert),
                true
            )
        }

//        }
    }

    private fun processBottleCreation(defaultLocation: LatLng) {
        showLoading()
        try {
            if (photo != null) {
                authViewModel.firebaseUser()?.let {
                    bottleViewModel.uploadBottleFile(photo!!, it)
                        .addOnSuccessListener { filename ->
                            createBottle(defaultLocation, filename)
                        }
                }
            } else {
                createBottle(defaultLocation, null)
            }

        } catch (e: Exception) {
            println("Gagal membuat status ${e.toString()}")
            hideLoading()
            CommonFunction.showSnackBar(
                binding.root,
                applicationContext,
                "Gagal membuat status : ${e.toString()}",
                //                    getString(R.string.file_failed_to_convert),
                true
            )
        }

    }

    private fun createBottle(defaultLocation: LatLng, filename: String?) {
        bottleViewModel.addBottle(
            if (myLocation != null) LatLng(
                myLocation!!.latitude,
                myLocation!!.longitude
            ) else defaultLocation,
            binding.etDescription.text.toString(),
            filename
        )
            .addOnCompleteListener {
                hideLoading()


                CommonFunction.showSnackBar(
                    binding.root,
                    applicationContext,
                    "Berhasil mengunggah!",
                    //                    getString(R.string.file_failed_to_convert),
                )
                onBackPressed()
            }
            .addOnFailureListener { exc ->
                hideLoading()
                CommonFunction.showSnackBar(
                    binding.root,
                    applicationContext,
                    "Gagal membuat status : $exc",
                    //                    getString(R.string.file_failed_to_convert),
                    true
                )

            }
    }

    private fun uploadBottleFile() {
        if (photo != null) {
            authViewModel.firebaseUser()?.let {
                bottleViewModel.uploadBottleFile(photo!!, it)
                    .addOnSuccessListener {

                    }
            }
        }
    }


}