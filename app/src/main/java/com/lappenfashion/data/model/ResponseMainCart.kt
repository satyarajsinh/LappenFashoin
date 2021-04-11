package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainCart(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: List<Payload?>?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
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
            @SerializedName("brand_fit")
            var brandFit: String?,
            @SerializedName("color")
            var color: String?,
            @SerializedName("color_code")
            var colorCode: String?,
            @SerializedName("country_of_origin")
            var countryOfOrigin: String?,
            @SerializedName("description")
            var description: String?,
            @SerializedName("detail_placement")
            var detailPlacement: String?,
            @SerializedName("fabric")
            var fabric: String?,
            @SerializedName("fabric_care")
            var fabricCare: String?,
            @SerializedName("group_id")
            var groupId: String?,
            @SerializedName("gst")
            var gst: Int?,
            @SerializedName("hsn_code")
            var hsnCode: String?,
            @SerializedName("ideal_for")
            var idealFor: String?,
            @SerializedName("image")
            var image: List<Image?>?,
            @SerializedName("inventory")
            var inventory: String?,
            @SerializedName("key_features")
            var keyFeatures: String?,
            @SerializedName("main_image_name")
            var mainImageName: String?,
            @SerializedName("mrp")
            var mrp: Int?,
            @SerializedName("occasion")
            var occasion: String?,
            @SerializedName("pack_of")
            var packOf: String?,
            @SerializedName("pattern")
            var pattern: String?,
            @SerializedName("pattern_print_type")
            var patternPrintType: String?,
            @SerializedName("pocket_style")
            var pocketStyle: String?,
            @SerializedName("print_coverage")
            var printCoverage: String?,
            @SerializedName("product_id")
            var productId: Int?,
            @SerializedName("product_name")
            var productName: String?,
            @SerializedName("product_style_id")
            var productStyleId: String?,
            @SerializedName("product_weight")
            var productWeight: String?,
            @SerializedName("reversible")
            var reversible: String?,
            @SerializedName("sale_price")
            var salePrice: Int?,
            @SerializedName("seller_sku_id")
            var sellerSkuId: String?,
            @SerializedName("size")
            var size: List<String?>?,
            @SerializedName("sleeve")
            var sleeve: String?,
            @SerializedName("sleeve_details")
            var sleeveDetails: String?,
            @SerializedName("sleeve_fit")
            var sleeveFit: String?,
            @SerializedName("sport_type")
            var sportType: String?,
            @SerializedName("surface_styling")
            var surfaceStyling: String?,
            @SerializedName("technology_used")
            var technologyUsed: String?,
            @SerializedName("type")
            var type: String?
        ) {
            data class Image(
                @SerializedName("image")
                var image: String?,
                @SerializedName("product_image_id")
                var productImageId: Int?
            )
        }
    }
}