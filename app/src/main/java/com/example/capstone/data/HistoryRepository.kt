package com.example.capstone.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.asFlow
import com.example.capstone.data.local.entity.AnalyzeHistory
import com.example.capstone.data.local.room.AnalyzeHistoryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

val ID_KEY = intPreferencesKey("user_id")
class HistoryRepository(private val dao: AnalyzeHistoryDao, private val dataStore: DataStore<Preferences>) {
    val userIdFlow: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[ID_KEY] ?: -1
        }

    fun getHistoryByUserId(): Flow<List<AnalyzeHistory>> = userIdFlow
        .flatMapLatest { userId ->
            if (userId != -1) {
                dao.getHistoryByUserId(userId).asFlow()
            } else {
                flowOf(emptyList())
            }
        }
}

