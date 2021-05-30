package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainOrderCanceled(
    @SerializedName("message")
    var message: String?,
    @SerializedName("result")
    var result: Boolean?
)