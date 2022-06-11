package com.pesanbotol.android.app.data.search.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.pesanbotol.android.app.data.search.model.SearchBottlesResponse
import com.pesanbotol.android.app.data.search.model.SearchMissionResponse
import com.pesanbotol.android.app.data.search.model.SearchResult
import com.pesanbotol.android.app.data.search.model.SearchUsersResponse

//import com.pesanbotol.android.app.data.search.model.SearchUsersResponse

enum class SearchKind { bottles, users, mission }
class SearchRepository {
    private var _functions: FirebaseFunctions = Firebase.functions

    fun search(q: String, searchKind: SearchKind): Task<Any?> {
        val query = hashMapOf(
            "page" to 1,
            "perPage" to 1,
            "q" to q,
            "searchKind" to searchKind.name,
        )
        return _functions.getHttpsCallable("searchTrigger-searchQuery").call(query).continueWith {
            it.result?.data
        }.addOnFailureListener {
            println("Error getting search result $it")
        }
    }

    fun searchBottles(q: String): Task<SearchBottlesResponse?> {
        val query = hashMapOf(
            "page" to 1,
            "perPage" to 250,
            "q" to q,
            "searchKind" to SearchKind.bottles.name,
        )
        return _functions
            .getHttpsCallable("searchTrigger-searchQuery")
            .call(query)
            .continueWith {
                val gson = Gson()
                Log.d("searchBottles result ",gson.toJson(it.result.data))
                val result =
                        gson.fromJson(
                            gson.toJson(it.result.data),
                            SearchBottlesResponse::class.java
                        )
                result
            }
            .addOnFailureListener {
                println("Error searchBottles result $it")
            }
    }

    fun searchUsers(q: String): Task<SearchUsersResponse?> {
        val query = hashMapOf(
            "page" to 1,
            "perPage" to 250,
            "q" to q,
            "searchKind" to SearchKind.users.name,
        )
        return _functions
            .getHttpsCallable("searchTrigger-searchQuery")
            .call(query)
            .continueWith {
                val gson = Gson()
                Log.d("searchUsers result ",gson.toJson(it.result.data))
                val result =
                        gson.fromJson(
                            gson.toJson(it.result.data),
                            SearchUsersResponse::class.java
                        )
                result
            }
            .addOnFailureListener {
                println("Error searchUsers result $it")
            }
    }

    fun searchMissions(q: String): Task<SearchMissionResponse?> {
        val query = hashMapOf(
            "page" to 1,
            "perPage" to 250,
            "q" to q,
            "searchKind" to SearchKind.mission.name,
        )
        return _functions
            .getHttpsCallable("searchTrigger-searchQuery")
            .call(query)
            .continueWith {
                val gson = Gson()
                Log.d("searchMissions result ",gson.toJson(it.result.data))
                val result =
                        gson.fromJson(
                            gson.toJson(it.result.data),
                            SearchMissionResponse::class.java
                        )
                result
            }
            .addOnFailureListener {
                println("Error searchMissions result $it")
            }
    }
}