package com.pesanbotol.android.app.data.auth.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull

class SessionPreferenceRepository private constructor(private val dataStore: DataStore<Preferences>) {

    private val _token = stringPreferencesKey("token")
    private val _user = stringPreferencesKey("user")
    private val _hasPassedOnboarding = booleanPreferencesKey("has-passed-onboarding")

    suspend fun getToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[_token] ?: ""
        }.firstOrNull()
    }

    fun getRealtimeToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[_token] ?: ""
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[_token] = token
        }
    }

    suspend fun saveUser(user: FirebaseUser) {
        val gson = Gson()
        dataStore.edit { preferences ->
            preferences[_user] = gson.toJson(user)
        }
    }

    suspend fun passedOnboarding() {
        dataStore.edit { preferences ->
            preferences[_hasPassedOnboarding] = true
        }
    }

    fun getOnboardingStatus(): Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            preferences[_hasPassedOnboarding]
        }
    }

    fun getUser(): Flow<FirebaseUser> {
        val gson = Gson()
        return dataStore.data.map { preferences ->
            gson.fromJson(preferences[_user], FirebaseUser::class.java)
        }
    }

    suspend fun removeToken() {
        dataStore.edit { preferences ->
            preferences.remove(_token)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionPreferenceRepository? = null

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreferenceRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreferenceRepository(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}