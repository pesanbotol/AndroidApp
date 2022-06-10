package com.pesanbotol.android.app.data.search.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.pesanbotol.android.app.data.search.model.SearchBottlesResponse
import com.pesanbotol.android.app.data.search.model.SearchUsersResponse

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

    fun searchBottles(q: String, searchKind: String? = "bottles"): Task<SearchBottlesResponse?> {
        val query = hashMapOf(
//            "data" to hashMapOf(
                "page" to 1,
                "perPage" to 1,
                "q" to q,
                "searchKind" to searchKind,
//            )
        )
        return _functions
            .getHttpsCallable("searchTrigger-searchQuery")
            .call(query)
            .continueWith {
                val gson = Gson()
                val result =
                    gson.fromJson(gson.toJson(it.result.data), SearchBottlesResponse::class.java)
                Log.e(
                    "LOG RESULT = ${result.result?.found}",
                    "${
                        result.result?.hits?.map { it?.document?.description ?: it?.document?.contentText ?: "null" }
                            ?.joinToString { " " }
                    }"
                )
                result
            }
            .addOnFailureListener {
                println("Error getting search result $it")
            }
    }

    fun searchUsers(q: String, searchKind: String? = "users"): Task<SearchUsersResponse?> {
        val query = hashMapOf(
//            "data" to hashMapOf(
            "page" to 1,
            "perPage" to 1,
            "q" to q,
            "searchKind" to searchKind,
//            )
        )
        return _functions
            .getHttpsCallable("searchTrigger-searchQuery")
            .call(query)
            .continueWith {
                it.result.data as SearchUsersResponse?
            }
            .addOnFailureListener {
                println("Error getting search result $it")
            }
    }

//    fun searchMissions(q: String, searchKind: String? = "users"): Task<SearchMsResponse?> {
//        val query = hashMapOf(
//            "page" to 1,
//            "perPage" to 1,
//            "q" to q,
//            "searchKind" to searchKind,
//        )
//        return _functions
//            .getHttpsCallable("searchTrigger-searchQuery")
//            .call(query)
//            .continueWith {
//                it.result.data as SearchUsersResponse?
//            }
//            .addOnFailureListener {
//                println("Error getting search result $it")
//            }
//    }
}