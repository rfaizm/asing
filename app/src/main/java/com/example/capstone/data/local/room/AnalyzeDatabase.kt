package com.example.capstone.data.local.room

import androidx.room.Database
import com.example.capstone.data.local.entity.AnalyzeHistory

@Database(entities = [AnalyzeHistory::class], version = 1)
abstract class AnalyzeDatabase {
    abstract fun analyzeHistoryDao() : AnalyzeHistoryDao
}