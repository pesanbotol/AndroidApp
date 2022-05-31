package com.pesanbotol.android.app.data.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.pesanbotol.android.app.data.profile.repository.ProfileRepository

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    fun getUpdateProfile(
        instagram: String?,
        facebook: String?,
        twitter: String?,
        displayName: String?,
        description: String?
    ) = profileRepository.getUpdateProfile(
        instagram,
        facebook,
        twitter,
        displayName,
        description
    )
}