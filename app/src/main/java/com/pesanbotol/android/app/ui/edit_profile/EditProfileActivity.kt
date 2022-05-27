package com.pesanbotol.android.app.ui.edit_profile

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.databinding.ActivityEditProfileBinding
import com.pesanbotol.android.app.ui.landing.fragment.AccountFragment
import com.pesanbotol.android.app.utility.CommonFunction
import com.pesanbotol.android.app.utility.uriToFile
import java.io.File

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding: ActivityEditProfileBinding? = null
    private val binding get() = _binding

    private var photo: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CommonFunction.REQUEST_CODE_PERMISSIONS) {
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

    private fun allPermissionsGranted() = CommonFunction.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (actionBar != null){
            actionBar?.setDisplayHomeAsUpEnabled(true)
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                CommonFunction.REQUIRED_PERMISSIONS,
                CommonFunction.REQUEST_CODE_PERMISSIONS
            )
        }
        binding?.ivGallery?.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(view: View?) {
        val dialogBottom = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_edit_profile_dialog, null)
        val btnGallery = view.findViewById<CardView>(R.id.btn_gallery_edit_profile)
        btnGallery.setOnClickListener {
            dialogBottom.dismiss()
            startGallery()
        }
        dialogBottom.setCancelable(true)
        dialogBottom.setContentView(view)
        dialogBottom.show()
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            photo = uriToFile(selectedImg, this@EditProfileActivity)
            binding?.profilePic?.setImageURI(selectedImg)
            binding?.profilePic?.background =
                ContextCompat.getDrawable(this@EditProfileActivity, R.drawable.rounded_outline)
        }
    }
}