package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainCartNew(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: Payload?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("cart_count")
        var cartCount: Int?,
        @SerializedName("cart_list")
        var cartList: List<Cart?>?,
        @SerializedName("total_cart_amount")
        var totalCartAmount: Int?
    ) {
        data class Cart(
            @SerializedName("amount")
            var amount: Int?,
            @SerializedName("cart_id")
            var cartId: Int?,
            @SerializedName("product")
            var product: Product?,
            @SerializedName("quantity")
            var quantity: Int?
        ) {
            data class Product(
                @SerializedName("color")
                var color: String?,
                @SerializedName("color_code")
                var colorCode: String?,
                @SerializedName("is_stock_available")
                var isStockAvailable: Int?,
                @SerializedName("main_image_name")
                var mainImageName: String?,
                @SerializedName("mrp")
                var mrp: Int?,
                @SerializedName("size_view_flag")
                var size_view_flag: Int?,
                @SerializedName("product_id")
                var productId: Int?,
                @SerializedName("product_name")
                var productName: String?,
                @SerializedName("sale_price")
                var salePrice: Int?,
                @SerializedName("size")
                var size: String?
            )
        }
    }
}