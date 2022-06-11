package com.pesanbotol.android.app.data.bottle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.pesanbotol.android.app.data.bottle.repository.BottleRepository
import java.io.File

class BottleViewModel(private val bottleRepository: BottleRepository) : ViewModel() {

//    private val _bottleViewState : MutableLiveData<>

    fun getBottle(latLng: LatLng) = bottleRepository.getBottles(latLng)

    @Throws(StorageException::class)
    fun deleteImage(storageReference: StorageReference) =
            bottleRepository.deleteImage(storageReference)

    fun uploadBottleFile(file: File, firebaseUser: FirebaseUser) =
            bottleRepository.uploadImage(file, firebaseUser)

    fun submitMission(latLng: LatLng) =
            bottleRepository.submitMission(latLng)

    fun addBottle(latLng: LatLng, content: String, filename: String?) =
            bottleRepository.addBottle(latLng, content, filename)
}