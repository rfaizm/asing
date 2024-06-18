package com.example.capstone.ui.home.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.ResultState
import com.example.capstone.data.UserRepository
import com.example.capstone.data.local.entity.AnalyzeHistory
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: UserRepository) : ViewModel() {

    val allAnalyses: LiveData<ResultState<List<AnalyzeHistory>>>

    init {
        allAnalyses = repository.getAllAnalyses()
    }

    fun insertAnalyzeHistory(history: AnalyzeHistory) {
        viewModelScope.launch {
            try {
                repository.insertAnalyzeHistory(history)
                Log.d("HistoryViewModel", "Insertion successful")
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Insertion error: ${e.message}")
            }
        }
    }
}
