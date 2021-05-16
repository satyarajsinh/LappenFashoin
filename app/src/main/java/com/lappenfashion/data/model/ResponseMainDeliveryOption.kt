package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainDeliveryOption(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: List<Payload?>?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("delivery_option_id")
        var deliveryOptionId: Int?,
        @SerializedName("description")
        var description: String?,
        @SerializedName("title")
        var title: String?
    )
}