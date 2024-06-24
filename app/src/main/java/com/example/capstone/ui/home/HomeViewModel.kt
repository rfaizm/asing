package com.example.capstone.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.capstone.data.UserRepository

class HomeViewModel(private val repository: UserRepository)  : ViewModel() {
    fun check() : String {
        return "Coba"
    }
    fun getCalories() = repository.getCalories()

    fun getHistory() = repository.getAllHistory()
}