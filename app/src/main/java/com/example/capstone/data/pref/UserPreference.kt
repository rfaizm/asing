package com.example.capstone.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = user.userId
            preferences[PHOTO_KEY] = user.photoUrl
            preferences[EMAIL_KEY] = user.email
            preferences[NAME_KEY] = user.name
            preferences[HEIGHT_KEY] = user.height
            preferences[WEIGHT_KEY] = user.weight
            preferences[AGE_KEY] = user.age
            preferences[HAND_KEY] = user.handCircle
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
            preferences[PROGRESS_KEY] = 0f
        }
    }

    suspend fun updateSession(user: UpdateModel) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[HEIGHT_KEY] = user.height
            preferences[WEIGHT_KEY] = user.weight
            preferences[AGE_KEY] = user.age
            preferences[HAND_KEY] = user.handCircle
        }
    }

    suspend fun updateProgress(user: UpdateProgress) {
        dataStore.edit { preferences ->
            preferences[PROGRESS_KEY] = user.calories
        }
    }


    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[ID_KEY] ?: "",
                preferences[PHOTO_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[HEIGHT_KEY] ?: 0f,
                preferences[WEIGHT_KEY] ?: 0f,
                preferences[AGE_KEY] ?: 0,
                preferences[HAND_KEY] ?: 0f,
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
            )
        }
    }

    fun getCalories(): Flow<UpdateProgress> {
        return dataStore.data.map { preferences->
            UpdateProgress(
                preferences[PROGRESS_KEY] ?: 0f
            )
        }
    }



    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun getAuthToken(): String? {
        return runBlocking {
            dataStore.data.first()[TOKEN_KEY]
        }
    }

    fun getLogin(): Boolean? {
        return runBlocking {
            dataStore.data.first()[IS_LOGIN_KEY]
        }
    }

    fun getCaloriesFloat(): Float? {
        return runBlocking {
            dataStore.data.first()[PROGRESS_KEY]
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val ID_KEY = stringPreferencesKey("userId")
        private val PHOTO_KEY = stringPreferencesKey("photoUrl")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val NAME_KEY = stringPreferencesKey("name")
        private val HEIGHT_KEY = floatPreferencesKey("height")
        private val WEIGHT_KEY = floatPreferencesKey("weight")
        private val AGE_KEY = intPreferencesKey("age")
        private val HAND_KEY = floatPreferencesKey("handCircle")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val PROGRESS_KEY = floatPreferencesKey("progress")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}