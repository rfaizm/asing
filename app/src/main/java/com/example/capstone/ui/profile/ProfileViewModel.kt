package com.example.capstone.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.UserRepository
import com.example.capstone.data.model.Profile
import com.example.capstone.data.pref.UserModel
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository : UserRepository) : ViewModel() {
    fun logoutSession() = repository.logoutSession()

    fun updateProfile(name : String, height : Float, weight : Float, age : Int, circleHand : Float) = repository.updateProfile(name, height, weight, age, circleHand)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }


}