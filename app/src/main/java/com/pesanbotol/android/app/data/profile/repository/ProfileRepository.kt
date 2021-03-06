package com.pesanbotol.android.app.data.profile.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.pesanbotol.android.app.data.bottle.model.ProfileUpdateResponse

class ProfileRepository {
    private var _functions: FirebaseFunctions = Firebase.functions
    fun getProfileById(id: String): Task<ProfileUpdateResponse?> {
        val query = hashMapOf(
            "uid" to id
        )
        return _functions
            .getHttpsCallable("authTrigger-profileByUid")
            .call(query)
            .continueWith { task ->
                val gson = Gson()
                val result = gson.fromJson(
                    gson.toJson(task.result?.data),
                    ProfileUpdateResponse::class.java
                )
                result
            }.addOnSuccessListener {
                println("Success getting $id profile")
            }.addOnFailureListener { ex ->
                println("Failure getting $id profile $ex")
            }
    }

    fun getMyProfile(): Task<ProfileUpdateResponse?> {
        return _functions
            .getHttpsCallable("authTrigger-myProfile")
            .call()
            .continueWith { task ->
                val gson = Gson()
                val result = gson.fromJson(
                    gson.toJson(task.result?.data),
                    ProfileUpdateResponse::class.java
                )
                result
            }.addOnSuccessListener {
                println("Success getting my profile")
            }.addOnFailureListener { ex ->
                println("Failure getting my profile $ex")
            }
    }

    fun updateProfile(
        instagram: String?,
        facebook: String?,
        twitter: String?,
        displayName: String?,
        description: String?
    ): Task<Boolean?> {
        val data = hashMapOf(
            "instagram" to instagram,
            "facebook" to facebook,
            "twitter" to twitter,
            "displayName" to displayName,
            "description" to description,
        )
        return _functions
            .getHttpsCallable("authTrigger-updateProfile")
            .call(data)
            .continueWith { task ->
                true
            }.addOnFailureListener {
                println("Failed to update profile : $it")
            }.addOnSuccessListener {
                println("Success to update profile : $it")
            }
    }

}