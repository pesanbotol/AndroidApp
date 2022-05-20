package com.pesanbotol.android.app.data.search.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

class SearchRepository {
    private var _functions: FirebaseFunctions = Firebase.functions

    fun search(q: String, searchKind: String? = "all"): Task<Any?> {
        val query = hashMapOf(
            "page" to 1,
            "perPage" to 1,
            "q" to q,
            "searchKind" to searchKind,
        )
        return _functions.getHttpsCallable("searchTrigger-searchQuery").call(query).continueWith {
            it.result?.data
        }.addOnFailureListener {
            println("Error getting search result $it")
        }
    }
}