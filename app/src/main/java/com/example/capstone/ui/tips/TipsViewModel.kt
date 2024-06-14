package com.example.capstone.ui.tips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.ResultState
import com.example.capstone.data.UserRepository
import com.example.capstone.data.api.response.DataItem
import kotlinx.coroutines.launch

class TipsViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _tipsLiveData = MediatorLiveData<ResultState<List<DataItem?>>>()
    val tipsLiveData: LiveData<ResultState<List<DataItem?>>> = _tipsLiveData

    init {
        getAllTips()
    }

    private fun getAllTips() {
        viewModelScope.launch {
            val source = userRepository.getAllTips()
            _tipsLiveData.addSource(source) { resultState ->
                when (resultState) {
                    is ResultState.Loading -> _tipsLiveData.value = ResultState.Loading
                    is ResultState.Success -> _tipsLiveData.value = ResultState.Success(resultState.data)
                    is ResultState.Error -> _tipsLiveData.value = ResultState.Error(resultState.error)
                }
            }
        }
    }
}