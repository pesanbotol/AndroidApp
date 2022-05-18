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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.databinding.ActivityAddMessageBinding
import com.pesanbotol.android.app.utility.CommonFunction.Companion.REQUEST_CODE_PERMISSIONS
import com.pesanbotol.android.app.utility.CommonFunction.Companion.REQUIRED_PERMISSIONS
import com.pesanbotol.android.app.utility.createCustomTempFile
import com.pesanbotol.android.app.utility.rotateBitmap
import com.pesanbotol.android.app.utility.uriToFile
import java.io.File

class AddMessageActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityAddMessageBinding
    private var photo: File? = null
    private lateinit var mMap: GoogleMap
    private var myLocation: Location? = null
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

        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
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
        if (mMap.isMyLocationEnabled) {
            mMap.setOnMyLocationClickListener {
                myLocation = it
                val latLng = LatLng(it.latitude, it.longitude)
                mMap.addMarker(
                    MarkerOptions().position(latLng)
                )
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
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
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
            val myFile = File(currentPhotoPath)

//            val result = BitmapFactory.decodeFile(myFile.path)
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                true
            )
            binding.previewImage.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddMessageActivity)
            binding.previewImage.setImageURI(selectedImg)
        }
    }


}