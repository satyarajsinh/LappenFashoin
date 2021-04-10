package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainLogin(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: String?,
    @SerializedName("result")
    var result: Boolean?
)