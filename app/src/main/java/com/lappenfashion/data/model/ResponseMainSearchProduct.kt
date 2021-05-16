package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainSearchProduct(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: List<Payload?>?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("product_name")
        var productName: String?
    )
}