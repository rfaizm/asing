package com.example.capstone.data.api.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)


data class LoginResponse(

	@field:SerializedName("loginResult")
	val loginResult: LoginResult? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class LoginResult(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("armCircumferenceCm")
	val armCircumferenceCm: Float? = null,

	@field:SerializedName("ageYears")
	val ageYears: Int? = null,

	@field:SerializedName("fullName")
	val fullName: String? = null,

	@field:SerializedName("heightCm")
	val heightCm: Float? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("weightKg")
	val weightKg: Float? = null,

	@field:SerializedName("token")
	val token: String? = null
)