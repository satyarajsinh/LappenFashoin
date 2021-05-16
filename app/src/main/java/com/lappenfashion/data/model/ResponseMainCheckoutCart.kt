package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainCheckoutCart(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: List<Payload?>?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("gst")
        var gst: Int?,
        @SerializedName("is_stock_available")
        var isStockAvailable: Int?,
        @SerializedName("main_image_name")
        var mainImageName: String?,
        @SerializedName("mrp")
        var mrp: Int?,
        @SerializedName("product_id")
        var productId: Int?,
        @SerializedName("product_name")
        var productName: String?,
        @SerializedName("sale_price")
        var salePrice: Int?
    )
}