package com.pesanbotol.android.app.ui.detail_users

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pesanbotol.android.app.R
import com.pesanbotol.android.app.data.profile.viewmodel.ProfileViewModel
import com.pesanbotol.android.app.databinding.ActivityDetailUsersBinding
import com.pesanbotol.android.app.ui.search.adapters.BadgeListAdapter
import com.pesanbotol.android.app.utility.CommonFunction
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailUsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUsersBinding
    private val profileViewModel by viewModel<ProfileViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUsersBinding.inflate(layoutInflater)
        intent.getStringExtra(UID_DATA)?.let {
            loadProfile(it)
            binding.refreshSwiper.setOnRefreshListener {
                binding.refreshSwiper.isRefreshing = false
                loadProfile(it)
            }
        }
        setContentView(binding.root)
    }

    private fun showLoading() {
//        binding?.progressBar?.visibility = View.VISIBLE
        binding.errorState.visibility = View.GONE
        binding.shimmerLayout.startShimmerAnimation()
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.profileLayout.visibility = View.GONE
    }

    private fun loadProfile(it: String) {
        showLoading()
        profileViewModel.getProfileById(it).addOnSuccessListener { profile ->
            hideLoading()
            supportActionBar?.title =
                    profile?.data?.displayName ?: profile?.data?.username?.toString()
            Glide.with(binding.root).load(profile?.data?.avatar?.mediaThumbnailUrl).apply(
                RequestOptions().error(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.empty_profile,
                    ),
                )
            ).into(binding.ivPhotoProfile)
            binding.tvUserLocation.visibility = View.GONE
            binding.tvFollowings.text =
                    "${profile?.data?.meta?.aggregator?.postCount ?: 0} Pesan Dibuat"
            binding.tvUserName.text =
                    profile?.data?.displayName ?: profile?.data?.username?.toString()
                            ?: ""
            binding.tvUserBio.text = profile?.data?.description ?: ""
            binding.tvSocmedFbId.text = profile?.data?.meta?.socials?.facebook ?: ""
            binding.tvSocmedIgId.text = profile?.data?.meta?.socials?.instagram ?: ""
            binding.tvSocmedTwitterId.text =
                    profile?.data?.meta?.socials?.twitter ?: ""
            binding.rvBadges.layoutManager = LinearLayoutManager(applicationContext)
            profile?.data?.meta?.badges?.let { badges ->
                binding.rvBadges.adapter = BadgeListAdapter(ArrayList(badges))
            }
        }.addOnFailureListener {
            hideLoading("Failed : ${it.message}")

            CommonFunction.showSnackBar(
                binding.root,
                applicationContext,
                "Gagal : ${it.message}",
                true
            )
        }
    }

    private fun hideLoading(error: String? = null) {
        binding.shimmerLayout.stopShimmerAnimation()
        binding.shimmerLayout.visibility = View.GONE
        if (error != null) {
            binding.errorState.visibility = View.VISIBLE
            binding.errorText.text = error
        } else {
            binding.errorState.visibility = View.GONE
            View.VISIBLE.also { binding.profileLayout.visibility = it }
        }
    }

    companion object {
        const val UID_DATA = "UID_DATA"
    }
}