package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainApplyCoupon(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: Payload?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("coupon_code")
        var couponCode: String?,
        @SerializedName("discount")
        var discount: Int?,
        @SerializedName("id")
        var id: Int?
    )
}