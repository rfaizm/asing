package com.example.capstone.data.api.response

import com.google.gson.annotations.SerializedName

data class ProgressGetResponse(

	@field:SerializedName("result")
	val result: List<ResultItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)


data class ResultItem(

	@field:SerializedName("progress")
	val progress: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: Timestamp? = null
)
