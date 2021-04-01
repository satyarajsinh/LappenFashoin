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
        @SerializedName("category_id")
        var categoryId: Int?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("sub_category")
        var subCategory: List<SubCategory?>?,
        @SerializedName("title")
        var title: String?
    ) {
        data class SubCategory(
            @SerializedName("category_id")
            var categoryId: Int?,
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("image")
            var image: String?,
            @SerializedName("product_category")
            var productCategory: List<ProductCategory?>?,
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
                @SerializedName("title")
                var title: String?
            )
        }
    }
}