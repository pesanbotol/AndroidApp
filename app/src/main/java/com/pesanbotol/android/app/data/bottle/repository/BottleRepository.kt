package com.pesanbotol.android.app.data.bottle.repository

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
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

    fun addBottle(
        firebaseUser: FirebaseUser,
        latLng: LatLng,
        content: String,
        file: File?
    ): Task<BottleCreatedResponse?> {
        val bottleRequest = hashMapOf(
            "contentText" to content,
            "kind" to "text",
            "geo" to listOf(latLng.latitude, latLng.longitude)
        )
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        lateinit var task: Task<BottleCreatedResponse?>
        if (file == null) {
            println("addBottle File Null")
            task = _functions
                .getHttpsCallable("bottle-callableBottle-createBottle")
                .call(bottleRequest)
                .continueWith {
                    val gson = Gson()
                    val result = gson.fromJson(
                        gson.toJson(it.result?.data),
                        BottleCreatedResponse::class.java
                    )
                    result
                }.addOnFailureListener {
                    println("addBottle Error Creating Bottle : $it")
                    throw it
                }
        } else {
            println("addBottle File Not Null")
            val filename = "${ts}.${file.extension}"
            val storageRef = _storage.reference.child("userupload").child(firebaseUser.uid)
                .child(filename)
            val uploadTask = storageRef.putBytes(file.readBytes())
            uploadTask.addOnFailureListener {
                println("addBottle Error uploading image $it")
                throw it
            }.addOnSuccessListener { it ->

                bottleRequest["contentImagePath"] = filename

                println("addBottle Success uploading image ${it.storage.downloadUrl}")
                task = _functions
                    .getHttpsCallable("bottle-callableBottle-createBottle")
                    .call(bottleRequest)
                    .continueWith {
                        val gson = Gson()
                        val result = gson.fromJson(
                            gson.toJson(it.result?.data),
                            BottleCreatedResponse::class.java
                        )
                        result
                    }.addOnFailureListener {
                        println("addBottle Error Creating Bottle : $it")
                    }
            }
        }
        return task

    }
}