package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainCategories(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: List<Payload?>?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("banner")
        var banner: String?,
        @SerializedName("bottom_banner")
        var bottomBanner: List<BottomBanner?>?,
        @SerializedName("category_id")
        var categoryId: Int?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("sub_category")
        var subCategory: List<SubCategory?>?,
        @SerializedName("thumbnail_image")
        var thumbnailImage: String?,
        @SerializedName("title")
        var title: String?,
        @SerializedName("top_banner")
        var topBanner: List<TopBanner?>?
    ) {
        data class BottomBanner(
            @SerializedName("banner_image")
            var bannerImage: String?
        )

        data class SubCategory(
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("image")
            var image: String?,
            @SerializedName("product_category")
            var productCategory: List<ProductCategory?>?,
            @SerializedName("sub_category_id")
            var subCategoryId: Int?,
            @SerializedName("thumbnail_image")
            var thumbnailImage: String?,
            @SerializedName("title")
            var title: String?
        ) {
            data class ProductCategory(
                @SerializedName("created_at")
                var createdAt: String?,
                @SerializedName("image")
                var image: String?,
                @SerializedName("product_category_id")
                var productCategoryId: Int?,
                @SerializedName("thumbnail_image")
                var thumbnailImage: String?,
                @SerializedName("title")
                var title: String?
            )
        }

        data class TopBanner(
            @SerializedName("banner_image")
            var bannerImage: String?
        )
    }
}