package com.pesanbotol.android.app.data.bottle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import com.pesanbotol.android.app.data.bottle.repository.BottleRepository
import java.io.File

class BottleViewModel(private val bottleRepository: BottleRepository) : ViewModel() {

//    private val _bottleViewState : MutableLiveData<>

    fun getBottle(latLng: LatLng) = bottleRepository.getBottles(latLng)
    fun addBottle(firebaseUser: FirebaseUser, latLng: LatLng, content: String, file: File?) =
            bottleRepository.addBottle(firebaseUser, latLng, content, file)
}