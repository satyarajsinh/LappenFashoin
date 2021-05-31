package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainNotification(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: List<Payload?>?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("description")
        var description: String?,
        @SerializedName("id")
        var id: Int?,
        @SerializedName("title")
        var title: String?
    )
}