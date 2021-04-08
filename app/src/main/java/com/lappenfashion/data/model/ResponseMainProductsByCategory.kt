package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseMainProductsByCategory(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: Payload?,
    @SerializedName("result")
    var result: Boolean?
) : Serializable {
    data class Payload(
        @SerializedName("data")
        var `data`: List<Data?>?,
        @SerializedName("links")
        var links: Links?,
        @SerializedName("meta")
        var meta: Meta?
    ) : Serializable{
        data class Data(
            @SerializedName("color")
            var color: String?,
            @SerializedName("description")
            var description: String?,
            @SerializedName("image")
            var image: List<Image?>?,
            @SerializedName("main_image_name")
            var mainImageName: String?,
            @SerializedName("mrp")
            var mrp: Int?,
            @SerializedName("product_id")
            var productId: Int?,
            @SerializedName("product_name")
            var productName: String?,
            @SerializedName("sale_price")
            var salePrice: Int?,
            @SerializedName("size")
            var size: List<String?>?
        ): Serializable {
            data class Image(
                @SerializedName("image")
                var image: String?,
                @SerializedName("product_image_id")
                var productImageId: Int?
            ): Serializable
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
        ): Serializable

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
        ): Serializable
    }
}