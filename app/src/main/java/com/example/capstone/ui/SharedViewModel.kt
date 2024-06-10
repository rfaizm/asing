package com.example.capstone.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _calories = MutableLiveData<Float>()
    val calories: LiveData<Float> get() = _calories

    fun setCalories(calories: Float) {
        _calories.value = calories
    }
}
