package com.pesanbotol.android.app.ui.landing.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pesanbotol.android.app.data.auth.viewmodel.AuthViewModel
import com.pesanbotol.android.app.data.profile.viewmodel.ProfileViewModel
import com.pesanbotol.android.app.databinding.FragmentAccountBinding
import com.pesanbotol.android.app.ui.edit_profile.EditProfileActivity
import com.pesanbotol.android.app.ui.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : Fragment(), View.OnClickListener {
    private val authViewModel by viewModel<AuthViewModel>()
    private val profileViewModel by viewModel<ProfileViewModel>()
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        binding?.tvEditProfile?.setOnClickListener(this)

        _binding?.let { view ->
            authViewModel.firebaseUser()?.let {
                Glide.with(view.root).load(it.photoUrl).into(view.ivPhotoProfile)
                view.tvUserName.text = it.displayName
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

        callProfileViewModel()

        return binding?.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun callProfileViewModel() {
        profileViewModel.getMyProfile()
            .addOnSuccessListener {
                binding?.apply {
                    tvEditProfile.text = it?.data?.username.toString()
                    tvUserBio.text = it?.data?.description
                }
            }
    }


    override fun onClick(view: View?) {
        val intent = Intent(requireContext(), EditProfileActivity::class.java)
        startActivity(intent)
    }

}