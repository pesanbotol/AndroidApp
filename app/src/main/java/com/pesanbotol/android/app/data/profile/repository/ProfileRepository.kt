package com.pesanbotol.android.app.data.profile.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

class ProfileRepository {
    private var _functions: FirebaseFunctions = Firebase.functions

    fun getUpdateProfile(
        instagram: String?,
        facebook: String?,
        twitter: String?,
        displayName: String?,
        description: String?
    ): Task<String?> {
        val data = hashMapOf(
            "instagram" to instagram,
            "facebook" to facebook,
            "twitter" to twitter,
            "displayName" to displayName,
            "description" to description,
        )
        return _functions
            .getHttpsCallable("authTrigger-myProfile")
            .call(data)
            .continueWith { task ->
                val result = task.result?.data as Map<String, Any>
                result["operationResult"] as String
            }.addOnFailureListener {
                println("addBottle Error Creating Bottle : $it")
            }
    }

}