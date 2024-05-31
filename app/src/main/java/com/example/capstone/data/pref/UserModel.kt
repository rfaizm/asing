package com.example.capstone.data.pref

data class UserModel(
    val userId: String,
    val photoUrl : String,
    val name : String,
    val height : Float,
    val weight : Float,
    val age : Int,
    val handCircle : Float,
    val token: String,
    val isLogin: Boolean = false
)