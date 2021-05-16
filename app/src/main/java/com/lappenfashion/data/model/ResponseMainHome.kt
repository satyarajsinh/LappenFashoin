package com.lappenfashion.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseMainHome(
    @SerializedName("message")
    var message: String?,
    @SerializedName("payload")
    var payload: Payload?,
    @SerializedName("result")
    var result: Boolean?
):Serializable {
    data class Payload(
        @SerializedName("cart_count")
        var cart_count: Int,
        @SerializedName("accessories")
        var accessories: List<Accessory?>?,
        @SerializedName("category_list")
        var categoryList: List<Category?>?,
        @SerializedName("deals_of_the_day")
        var dealsOfTheDay: List<DealsOfTheDay?>?,
        @SerializedName("explore_list")
        var exploreList: List<Explore?>?,
        @SerializedName("offer_poster")
        var offerPoster: List<OfferPoster?>?,
        @SerializedName("other_poster")
        var otherPoster: List<OtherPoster?>?,
        @SerializedName("sub_category")
        var subCategory: List<SubCategory?>?,
        @SerializedName("trending")
        var trending: List<Trending?>?
    ) :Serializable{
        data class Accessory(
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("image")
            var image: String?,
            @SerializedName("product_category")
            var productCategory: List<Any?>?,
            @SerializedName("sub_category_id")
            var subCategoryId: Int?,
            @SerializedName("thumbnail_image")
            var thumbnailImage: String?,
            @SerializedName("title")
            var title: String?
        ):Serializable

        data class Category(
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
        ):Serializable {
            data class BottomBanner(
                @SerializedName("banner_image")
                var bannerImage: String?
            ):Serializable

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
            ) :Serializable{
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
                ):Serializable
            }

            data class TopBanner(
                @SerializedName("banner_image")
                var bannerImage: String?
            ):Serializable
        }

        data class DealsOfTheDay(
            @SerializedName("amount")
            var amount: Int?,
            @SerializedName("category_id")
            var categoryId: Int?,
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("deal_id")
            var dealId: Int?,
            @SerializedName("image")
            var image: String?,
            @SerializedName("product_category_id")
            var productCategoryId: Int?,
            @SerializedName("sub_category_id")
            var subCategoryId: Int?,
            @SerializedName("title")
            var title: String?
        ):Serializable

        data class Explore(
            @SerializedName("amount")
            var amount: Int?,
            @SerializedName("category_id")
            var categoryId: Int?,
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("explore_id")
            var exploreId: Int?,
            @SerializedName("image")
            var image: String?,
            @SerializedName("product_category_id")
            var productCategoryId: Int?,
            @SerializedName("sub_category_id")
            var subCategoryId: Int?
        ) :Serializable

        data class OfferPoster(
            @SerializedName("image")
            var image: String?,
            @SerializedName("thumbnail_image")
            var thumbnailImage: String?
        ):Serializable

        data class OtherPoster(
            @SerializedName("image")
            var image: String?,
            @SerializedName("thumbnail_image")
            var thumbnailImage: String?
        ):Serializable

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
        ) :Serializable{
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
            ):Serializable
        }

        data class Trending(
            @SerializedName("image")
            var image: String?,
            @SerializedName("price")
            var price: Int?,
            @SerializedName("sale_price")
            var salePrice: Int?,
            @SerializedName("thumbnail_image")
            var thumbnailImage: String?
        ):Serializable
    }
}