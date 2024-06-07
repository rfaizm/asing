package com.example.capstone.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstone.data.HistoryRepository
import com.example.capstone.data.local.entity.AnalyzeHistory

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {
    val historyList: LiveData<List<AnalyzeHistory>> = repository.getHistoryByUserId().asLiveData()
}


