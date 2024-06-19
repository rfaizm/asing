package com.example.capstone.data.api.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(

	@field:SerializedName("decoded")
	val decoded: Decoded? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Payload(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("iat")
	val iat: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class Raw(

	@field:SerializedName("payload")
	val payload: String? = null,

	@field:SerializedName("signature")
	val signature: String? = null,

	@field:SerializedName("header")
	val header: String? = null
)

data class Header(

	@field:SerializedName("typ")
	val typ: String? = null,

	@field:SerializedName("alg")
	val alg: String? = null
)

data class Decoded(

	@field:SerializedName("decoded")
	val decoded: Decoded? = null,

	@field:SerializedName("raw")
	val raw: Raw? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("payload")
	val payload: Payload? = null,

	@field:SerializedName("signature")
	val signature: String? = null,

	@field:SerializedName("header")
	val header: Header? = null
)
