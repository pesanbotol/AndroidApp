package com.pesanbotol.android.app.data.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.pesanbotol.android.app.data.bottle.model.ProfileUpdateResponse
import com.pesanbotol.android.app.data.core.StateHandler
import com.pesanbotol.android.app.data.profile.repository.ProfileRepository

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    private val _myProfile = MutableLiveData<ProfileUpdateResponse>()
    val myProfile: LiveData<ProfileUpdateResponse?> = _myProfile

    fun getMyProfile() = profileRepository.getMyProfile()

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