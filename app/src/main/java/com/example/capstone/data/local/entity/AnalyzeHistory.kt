package com.example.capstone.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analyze_history")
data class AnalyzeHistory (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "image_uri") val imageUri: String,
    @ColumnInfo(name = "analyze_result") val analyzeResult: String,
    @ColumnInfo(name = "nutrition") val nutrition: String,
    @ColumnInfo(name = "confidence_score") val confidenceScore: Float
)