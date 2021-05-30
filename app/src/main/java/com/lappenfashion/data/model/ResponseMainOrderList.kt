package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseMainOrderList(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: Payload?,
    @SerializedName("result")
    var result: Boolean?
) :Serializable{
    data class Payload(
        @SerializedName("data")
        var `data`: List<Data?>?,
        @SerializedName("links")
        var links: Links?,
        @SerializedName("meta")
        var meta: Meta?
    ) :Serializable{
        data class Data(
            @SerializedName("grand_total")
            var grandTotal: Double?,
            @SerializedName("shipping_fee")
            var shipping_fee: Int?,
            @SerializedName("extra_discount")
            var extra_discount: Double?,
            @SerializedName("list_price")
            var list_price: Double?,
            @SerializedName("selling_price")
            var selling_price: Double?,
            @SerializedName("order_status_detail")
            var orderStatusDetail: List<OrderStatusDetail?>?,
            @SerializedName("order_id")
            var orderId: Int?,
            @SerializedName("products")
            var products: List<Product?>?,
            @SerializedName("status")
            var status: String?
        ) :Serializable{
            data class OrderStatusDetail(
                @SerializedName("created_at")
                var createdAt: String?,
                @SerializedName("description")
                var description: String?,
                @SerializedName("id")
                var id: Int?,
                @SerializedName("status")
                var status: String?
            ) : Serializable
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
            ):Serializable
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
        ):Serializable

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
        ):Serializable
    }
}