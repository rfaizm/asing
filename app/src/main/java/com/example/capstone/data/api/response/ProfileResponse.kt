package com.example.capstone.data.api.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("result")
	val result: Result? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Result(

	@field:SerializedName("armCircumferenceCm")
	val armCircumferenceCm: Float? = null,

	@field:SerializedName("ageYears")
	val ageYears: Int? = null,

	@field:SerializedName("fullName")
	val fullName: String? = null,

	@field:SerializedName("weightKg")
	val weightKg: Float? = null,

	@field:SerializedName("heightCm")
	val heightCm: Float? = null
)
