package com.example.capstone.data.api.response

import com.google.gson.annotations.SerializedName

data class NutriotionResponse(

	@field:SerializedName("data")
	val data: NutritionResult? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class NutritionResult(

	@field:SerializedName("proteins")
	val proteins: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("fat")
	val fat: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("calories")
	val calories: Int? = null,

	@field:SerializedName("carbohydrate")
	val carbohydrate: Float? = null
)
