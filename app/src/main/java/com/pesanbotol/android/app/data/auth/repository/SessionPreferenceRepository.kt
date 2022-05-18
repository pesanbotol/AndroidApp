package com.pesanbotol.android.app.data.auth.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.google.gson.Gson
import com.pesanbotol.android.app.data.auth.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull

class SessionPreferenceRepository private constructor(private val dataStore: DataStore<Preferences>) {

    private val _token = stringPreferencesKey("token")
    private val _user_name = stringPreferencesKey("user-name")
    private val _user_photo = stringPreferencesKey("user-photo")
    private val _user_email = stringPreferencesKey("user-email")
    private val _user_uid = stringPreferencesKey("user-uid")
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

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[_user_uid] = user.uid ?: ""
            preferences[_user_name] = user.name ?: ""
            preferences[_user_photo] = user.photoUrl ?: ""
            preferences[_user_email] = user.email ?: ""
        }
    }

    suspend fun removeUser() {
        dataStore.edit { preferences ->
            preferences.remove(_user_uid)
            preferences.remove(_user_name)
            preferences.remove(_user_photo)
            preferences.remove(_user_email)
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

    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                uid = preferences[_user_uid],
                name = preferences[_user_name],
                email = preferences[_user_email],
                photoUrl = preferences[_user_photo]
            )
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