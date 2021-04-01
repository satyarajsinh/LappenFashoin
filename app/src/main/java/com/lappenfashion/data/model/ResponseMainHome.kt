package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName

data class ResponseMainHome(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: Payload?,
    @SerializedName("result")
    var result: Boolean?
) {
    data class Payload(
        @SerializedName("category_list")
        var categoryList: List<Category?>?,
        @SerializedName("deals_of_the_day")
        var dealsOfTheDay: List<DealsOfTheDay?>?,
        @SerializedName("explore_list")
        var exploreList: List<Explore?>?
    ) {
        data class Category(
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

        data class DealsOfTheDay(
            @SerializedName("amount_type")
            var amountType: String?,
            @SerializedName("category_ids")
            var categoryIds: String?,
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("deal_id")
            var dealId: Int?,
            @SerializedName("image")
            var image: String?,
            @SerializedName("title")
            var title: String?,
            @SerializedName("value")
            var value: String?
        )

        data class Explore(
            @SerializedName("amount_type")
            var amountType: String?,
            @SerializedName("category_ids")
            var categoryIds: String?,
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("explore_id")
            var exploreId: Int?,
            @SerializedName("image")
            var image: String?,
            @SerializedName("shot_description")
            var shotDescription: String?,
            @SerializedName("title")
            var title: String?,
            @SerializedName("value")
            var value: String?
        )
    }
}