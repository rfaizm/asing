package com.example.capstone.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.capstone.data.local.entity.AnalyzeHistory

@Database(entities = [AnalyzeHistory::class], version = 1)
abstract class AnalyzeDatabase : RoomDatabase() {
    abstract fun analyzeHistoryDao(): AnalyzeHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AnalyzeDatabase? = null

        fun getDatabase(context: Context): AnalyzeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnalyzeDatabase::class.java,
                    "history_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
