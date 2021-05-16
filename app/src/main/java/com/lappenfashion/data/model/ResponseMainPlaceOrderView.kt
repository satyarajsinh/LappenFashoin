package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainPlaceOrderView(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: Payload?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("address")
        var address: Address?,
        @SerializedName("cart_count")
        var cartCount: Int?,
        @SerializedName("cart_list")
        var cartList: List<Cart?>?,
        @SerializedName("delivery_option")
        var deliveryOption: DeliveryOption?,
        @SerializedName("total_cart_amount")
        var totalCartAmount: Int?
    ) {
        data class Address(
            @SerializedName("address")
            var address: String?,
            @SerializedName("address_id")
            var addressId: Int?,
            @SerializedName("city")
            var city: String?,
            @SerializedName("locality_town")
            var localityTown: String?,
            @SerializedName("mobile_number")
            var mobileNumber: String?,
            @SerializedName("name")
            var name: String?,
            @SerializedName("pincode")
            var pincode: String?,
            @SerializedName("state")
            var state: String?,
            @SerializedName("type")
            var type: String?
        )

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
                @SerializedName("is_stock_available")
                var isStockAvailable: Int?,
                @SerializedName("main_image_name")
                var mainImageName: String?,
                @SerializedName("mrp")
                var mrp: Int?,
                @SerializedName("product_id")
                var productId: Int?,
                @SerializedName("size")
                var size: Int?,
                @SerializedName("color_code")
                var color_code: Int?,
                @SerializedName("product_name")
                var productName: String?,
                @SerializedName("sale_price")
                var salePrice: Int?
            )
        }

        data class DeliveryOption(
            @SerializedName("delivery_option_id")
            var deliveryOptionId: Int?,
            @SerializedName("description")
            var description: String?,
            @SerializedName("title")
            var title: String?
        )
    }
}