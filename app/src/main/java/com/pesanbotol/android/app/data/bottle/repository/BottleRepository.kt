package com.pesanbotol.android.app.data.bottle.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

class BottleRepository {
    private var _functions: FirebaseFunctions = Firebase.functions

    fun getBottles(): Task<Any?> {
        return _functions
            .getHttpsCallable("bottle-callableBottle-indexBottleByGeocord")
            .call()
            .continueWith {
                val result = it.result?.data
                println("RESULTWLWLKWLWKWK ${result}")
                result
            }.addOnCompleteListener {
                println("RESULTWLWLKWLWKWK ${it}")
            }
    }
}