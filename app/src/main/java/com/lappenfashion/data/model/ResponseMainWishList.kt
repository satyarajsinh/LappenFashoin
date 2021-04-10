package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainWishList(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: List<Payload?>?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("product")
        var product: Product?,
        @SerializedName("wish_list_id")
        var wishListId: Int?
    ) {
        data class Product(
            @SerializedName("color")
            var color: String?,
            @SerializedName("description")
            var description: String?,
            @SerializedName("image")
            var image: List<Any?>?,
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
        )
    }
}