package com.example.capstone.ui.tips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.ResultState
import com.example.capstone.data.UserRepository
import com.example.capstone.data.api.response.Data
import kotlinx.coroutines.launch

class TipsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _tipsLiveData = MutableLiveData<ResultState<List<Data>>>()
    val tipsLiveData: LiveData<ResultState<List<Data>>> = _tipsLiveData

    fun loadTips() {
        viewModelScope.launch {
            _tipsLiveData.value = ResultState.Loading
            repository.getTips().observeForever { resultState ->
                when (resultState) {
                    is ResultState.Success -> {
                        _tipsLiveData.value = ResultState.Success(resultState.data?.data ?: emptyList())
                    }
                    is ResultState.Error -> {
                        _tipsLiveData.value = ResultState.Error(resultState.error)
                    }
                    is ResultState.Loading -> {
                        // Handle loading state if necessary
                    }
                }
            }
        }
    }
}