package com.example.capstone.di

import android.content.Context
import com.example.capstone.data.UserRepository
import com.example.capstone.data.api.config.ApiConfig
import com.example.capstone.data.local.room.AnalyzeDatabase
import com.example.capstone.data.pref.UserPreference
import com.example.capstone.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val database = AnalyzeDatabase.getDatabase(context)
        val analyzeHistoryDao = database.analyzeHistoryDao()
        return UserRepository.getInstance(apiService, pref, analyzeHistoryDao)
    }
}