package com.pesanbotol.android.app.data.auth.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.pesanbotol.android.app.data.response.GeoItem

class AddMessageBottleRepository() {
    private lateinit var functions: FirebaseFunctions

    fun addMessage(contextText: String): Task<Any?> {
        // Create the arguments to the callable function.
        val data = hashMapOf(
            "" to contextText,
            "push" to true
        )

        return functions
            .getHttpsCallable(
                "bottle-callableBottle-createBottl"
            )
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as String
                result
            }
    }
}