package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainVersionUpdate(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: Payload?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("app_version")
        var appVersion: String?
    )
}