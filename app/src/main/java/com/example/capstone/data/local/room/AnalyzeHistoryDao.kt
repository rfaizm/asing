package com.example.capstone.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.capstone.data.local.entity.AnalyzeHistory

@Dao
    interface AnalyzeHistoryDao {
        @Insert
        fun insert(history: AnalyzeHistory)

        @Query("SELECT * FROM analyze_history")
        fun getAllAnalyses(): LiveData<List<AnalyzeHistory>>
    }
