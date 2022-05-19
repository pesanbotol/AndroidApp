package com.pesanbotol.android.app.data.bottle.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions

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
                "bottle-callableBottle-createBottle"
            )
            .call(data)
            .continueWith { task ->
                val result = task.result?.data as String
                result
            }
    }
}