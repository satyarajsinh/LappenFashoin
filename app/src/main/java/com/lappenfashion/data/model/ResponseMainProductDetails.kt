package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainProductDetails(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: Payload?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("brand_fit")
        var brandFit: String?,
        @SerializedName("material")
        var material: String?,
        @SerializedName("color")
        var color: String?,
        @SerializedName("color_code")
        var colorCode: String?,
        @SerializedName("color_list")
        var colorList: List<Color?>?,
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
        @SerializedName("is_stock_available")
        var isStockAvailable: Int?,
        @SerializedName("is_cart")
        var is_cart: Int?,
        @SerializedName("size_view_flag")
        var size_view_flag: Int?,
        @SerializedName("is_wish_list")
        var isWishList: Int?,
        @SerializedName("key_features")
        var keyFeatures: String?,
        @SerializedName("main_image_name")
        var mainImageName: String?,
        @SerializedName("size_chart_image")
        var size_chart_image: String?,
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
        @SerializedName("discount")
        var discount: Int?,
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
        var size: String?,
        @SerializedName("size_list")
        var sizeList: List<Size?>?,
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
        @SerializedName("rating_avg")
        var rating_avg: Double?,
        @SerializedName("reviews")
        var reviews: List<Review?>?,
        @SerializedName("type")
        var type: String?
    ) {
        data class Color(
            @SerializedName("color")
            var color: String?,
            @SerializedName("color_code")
            var colorCode: String?,
            @SerializedName("product_id")
            var productId: Int?,
            var flag : Int = 0
        )

        data class Image(
            @SerializedName("image")
            var image: String?,
            @SerializedName("product_image_id")
            var productImageId: Int?
        )

        data class Review(
            @SerializedName("ratting")
            var ratting: Int?,
            @SerializedName("review")
            var review: String?,
            @SerializedName("review_id")
            var reviewId: Int?,
            @SerializedName("user")
            var user: User?
        ) {
            data class User(
                @SerializedName("birth_date")
                var birthDate: String?,
                @SerializedName("email")
                var email: String?,
                @SerializedName("gender")
                var gender: String?,
                @SerializedName("image")
                var image: String?,
                @SerializedName("is_profile_setup")
                var isProfileSetup: Int?,
                @SerializedName("mobile_number")
                var mobileNumber: String?,
                @SerializedName("name")
                var name: String?,
                @SerializedName("user_id")
                var userId: Int?
            )
        }

        data class Size(
            @SerializedName("product_id")
            var productId: Int?,
            @SerializedName("size")
            var size: String?
        )
    }
}