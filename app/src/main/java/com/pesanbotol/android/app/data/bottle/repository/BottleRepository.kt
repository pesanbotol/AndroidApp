package com.pesanbotol.android.app.data.bottle.repository

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.pesanbotol.android.app.data.bottle.model.BottleContentResponse
import com.pesanbotol.android.app.data.bottle.model.BottleCreatedResponse

class BottleRepository {
    private var _functions: FirebaseFunctions = Firebase.functions

    fun getBottles(latLng: LatLng): Task<BottleContentResponse?> {

        val position = hashMapOf(
            "geo" to listOf(latLng.latitude.toString(), latLng.longitude.toString())
        )
        return _functions
            .getHttpsCallable("bottle-callableBottle-indexBottleByGeocord")
            .call(position)
            .continueWith {
                val gson = Gson()
                val result = gson.fromJson(
                    gson.toJson(it.result?.data),
                    BottleContentResponse::class.java
                )
                result
            }.addOnSuccessListener {
                println("Success getting bottles")
            }
    }

    fun addBottle(latLng: LatLng, content: String): Task<BottleCreatedResponse?> {
        val position = hashMapOf(
            "contentText" to content,
            "kind" to "text",
            "geo" to listOf(latLng.latitude, latLng.longitude)
        )
        return _functions
            .getHttpsCallable("bottle-callableBottle-createBottle")
            .call(position)
            .continueWith {
                val gson = Gson()
                val result = gson.fromJson(
                    gson.toJson(it.result?.data),
                    BottleCreatedResponse::class.java
                )
                result
            }.addOnFailureListener { 
                println("Error Creating Bottle : $it")
            }

    }
}