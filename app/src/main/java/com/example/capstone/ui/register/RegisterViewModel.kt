package com.example.capstone.ui.register

import androidx.lifecycle.ViewModel
import com.example.capstone.data.UserRepository

class RegisterViewModel(private val repository : UserRepository) : ViewModel()  {
    fun register(email : String, password : String, name : String, height : Float, weight : Float, age : Int, circleHand : Float) =
        repository.register(email, password, name, height, weight, age, circleHand)
}