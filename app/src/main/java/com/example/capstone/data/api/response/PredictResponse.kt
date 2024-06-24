package com.example.capstone.data.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class PredictResponse(

	@field:SerializedName("data")
	val data: DataPredict? = null,

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class DataPredict(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("confidenceScore")
	val confidenceScore: String? = null,

	@field:SerializedName("userFullName")
	val userFullName: String? = null,

	@field:SerializedName("recommendation")
	val recommendation: String? = null,

	@field:SerializedName("predictedClassName")
	val predictedClassName: String? = null,

	@field:SerializedName("userEmail")
	val userEmail: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
) : Parcelable
