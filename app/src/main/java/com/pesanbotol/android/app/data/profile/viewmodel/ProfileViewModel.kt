package com.pesanbotol.android.app.data.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pesanbotol.android.app.data.bottle.model.ProfileUpdateResponse
import com.pesanbotol.android.app.data.core.StateHandler
import com.pesanbotol.android.app.data.profile.repository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            _myProfile = MutableLiveData<StateHandler<ProfileUpdateResponse>>()
            getMyProfile()
        }
    }

    private lateinit var _myProfile: MutableLiveData<StateHandler<ProfileUpdateResponse>>
    val myProfile: LiveData<StateHandler<ProfileUpdateResponse>> = _myProfile

    fun getMyProfile() {
        _myProfile.postValue(StateHandler.Loading())
        try {
            profileRepository.getMyProfile()
                .addOnSuccessListener {
                    _myProfile.postValue(StateHandler.Success(it))
                }
                .addOnFailureListener {
                    _myProfile.postValue(StateHandler.Error(it.message))
                }
        } catch (e: Exception) {
            _myProfile.postValue(StateHandler.Error(e.message))
        }
    }


    fun updateProfile(
        instagram: String?,
        facebook: String?,
        twitter: String?,
        displayName: String?,
        description: String?
    ) = profileRepository.updateProfile(
        instagram,
        facebook,
        twitter,
        displayName,
        description
    )

    fun getProfileById(
        id: String
    ) = profileRepository.getProfileById(
        id
    )
}