package com.example.gagalmuluyaallah.model

import com.google.gson.annotations.SerializedName

data class GeneralResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
