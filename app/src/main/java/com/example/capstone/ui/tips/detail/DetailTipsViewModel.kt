package com.example.capstone.ui.tips.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.ResultState
import com.example.capstone.data.UserRepository
import com.example.capstone.data.api.response.Data
import kotlinx.coroutines.launch

class DetailTipsViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _tipId = MutableLiveData<String?>()
    private val _detailTips = MediatorLiveData<ResultState<Data>>()

    val detailTips: LiveData<ResultState<Data>> = _detailTips

    init {
        _detailTips.addSource(_tipId) { id ->
            if (id != null) {
                fetchDetailTips(id)
            }
        }
    }

    fun getDetailTips(tipId: String) {
        Log.d("DetailTipsViewModel", "Setting Tip ID: $tipId")
        _tipId.value = tipId
    }

    private fun fetchDetailTips(id: String) {
        viewModelScope.launch {
            _detailTips.postValue(ResultState.Loading)
            try {
                userRepository.getDetailTips(id).asFlow().collect { result ->
                    Log.d("DetailTipsViewModel", "Result: $result")
                    _detailTips.postValue(result)
                }
            } catch (e: Exception) {
                _detailTips.postValue(ResultState.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _tipId.value = null
    }
}