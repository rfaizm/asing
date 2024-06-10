package com.example.capstone.ui.analyze

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.UserRepository
import kotlinx.coroutines.launch
import java.io.File

class AnalyzeViewModel(private val repository : UserRepository)  : ViewModel() {
    fun uploadImage(file : File) = repository.uploadImage(file)

    fun getNutrition(name  : String) = repository.getNutrition(name)
}