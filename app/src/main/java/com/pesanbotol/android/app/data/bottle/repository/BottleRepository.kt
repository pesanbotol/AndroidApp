package com.pesanbotol.android.app.data.bottle.repository

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.pesanbotol.android.app.data.bottle.model.BottleContentResponse
import com.pesanbotol.android.app.data.bottle.model.BottleCreatedResponse
import java.io.File

class BottleRepository {
    private var _functions: FirebaseFunctions = Firebase.functions
    private var _storage: FirebaseStorage = Firebase.storage

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

    fun uploadImage(file: File, firebaseUser: FirebaseUser): Task<String?> {
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        val filename = "${ts}.${file.extension}"
        val storageRef = _storage.reference.child("userupload").child(firebaseUser.uid)
            .child(filename)
        val uploadTask = storageRef.putBytes(file.readBytes())
        return uploadTask.addOnFailureListener {
            println("addBottle uploadBottleImage Error uploading image $it")
            throw it
        }.addOnSuccessListener { it ->

            println("addBottle uploadBottleImage Success uploading image ${it.storage.downloadUrl}")

        }.continueWith { filename }
    }

    fun addBottle(
        latLng: LatLng,
        content: String,
        filename: String?
    ): Task<Any?> {
        val bottleRequest = hashMapOf(
            "contentText" to content,
            "kind" to "text",
            "geo" to listOf(latLng.latitude, latLng.longitude)
        )


        if (filename != null) {
            println("addBottle with Image : $filename")
            bottleRequest["contentImagePath"] = filename
        }
        return _functions
            .getHttpsCallable("bottle-callableBottle-createBottle")
            .call(bottleRequest)
            .continueWith {
                it.result.data
            }.addOnFailureListener {
                println("addBottle Error Creating Bottle : $it")
            }

    }

}