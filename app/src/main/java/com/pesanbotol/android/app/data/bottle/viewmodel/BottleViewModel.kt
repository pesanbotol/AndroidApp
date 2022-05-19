package com.pesanbotol.android.app.data.bottle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pesanbotol.android.app.data.bottle.repository.BottleRepository

class BottleViewModel(private val bottleRepository: BottleRepository) : ViewModel() {

//    private val _bottleViewState : MutableLiveData<>

    fun getBottle() = bottleRepository.getBottles()
}