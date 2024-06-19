package com.example.capstone.data.api.response

import com.google.gson.annotations.SerializedName

data class ProgressResponse(

	@field:SerializedName("result")
	val result: ProgressResult? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Timestamp(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("time")
	val time: String? = null
)

data class ProgressResult(

	@field:SerializedName("progress")
	val progress: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: Timestamp? = null
)
