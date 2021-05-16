package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainOrderList(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: Payload?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("data")
        var `data`: List<Data?>?,
        @SerializedName("links")
        var links: Links?,
        @SerializedName("meta")
        var meta: Meta?
    ) {
        data class Data(
            @SerializedName("grand_total")
            var grandTotal: Int?,
            @SerializedName("order_id")
            var orderId: Int?,
            @SerializedName("products")
            var products: List<Product?>?,
            @SerializedName("status")
            var status: String?
        ) {
            data class Product(
                @SerializedName("color")
                var color: String?,
                @SerializedName("color_code")
                var colorCode: String?,
                @SerializedName("main_image_name")
                var mainImageName: String?,
                @SerializedName("mrp")
                var mrp: Int?,
                @SerializedName("order_product_id")
                var orderProductId: Int?,
                @SerializedName("product_name")
                var productName: String?,
                @SerializedName("quantity")
                var quantity: Int?,
                @SerializedName("sale_price")
                var salePrice: Int?,
                @SerializedName("size")
                var size: String?,
                @SerializedName("total_amount")
                var totalAmount: Int?
            )
        }

        data class Links(
            @SerializedName("first")
            var first: String?,
            @SerializedName("last")
            var last: String?,
            @SerializedName("next")
            var next: Any?,
            @SerializedName("prev")
            var prev: Any?
        )

        data class Meta(
            @SerializedName("current_page")
            var currentPage: Int?,
            @SerializedName("from")
            var from: Int?,
            @SerializedName("last_page")
            var lastPage: Int?,
            @SerializedName("path")
            var path: String?,
            @SerializedName("per_page")
            var perPage: Int?,
            @SerializedName("to")
            var to: Int?,
            @SerializedName("total")
            var total: Int?
        )
    }
}