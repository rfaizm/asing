package com.example.capstone.data.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class HistoryResponse(

	@field:SerializedName("data")
	val data: List<DataHistory?>? = null
)

data class DataHistory(

	@field:SerializedName("predictId")
	val predictId: String? = null,

	@field:SerializedName("history")
	val history: History? = null
)
@Parcelize
data class History(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("confidenceScore")
	val confidenceScore: Float? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("userFullName")
	val userFullName: String? = null,

	@field:SerializedName("recommendation")
	val recommendation: String? = null,

	@field:SerializedName("predictedClassName")
	val predictedClassName: String? = null,

	@field:SerializedName("userEmail")
	val userEmail: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
) : Parcelable
