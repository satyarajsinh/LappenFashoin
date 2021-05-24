package com.lappenfashion.data.network

import android.content.Context
import com.example.simplemvvm.utils.Constants
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lappenfashion.data.model.*
import okhttp3.Cache
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface MyApi {

    /*@GET(Constants.END_POINT)
    fun getCategories(): Call<ResponseMain>
*/
    @GET
    fun getHome(@Url url: String): Call<JsonObject>

    @GET
    fun searchProduct(@Url url: String): Call<ResponseMainSearchProduct>

    @GET(Constants.END_POINT_PLACE_ORDER)
    fun getOrders(@Header("Authorization") token: String): Call<ResponseMainOrderList>

    @GET
    fun getProducts(@Url url: String): Call<JsonObject>

    @GET
    fun getProductDetails(@Url url: String): Call<ResponseMainProductDetails>

    @GET(Constants.END_POINT_CATEGORY)
    fun getCategories(): Call<ResponseMainCategories>

    @GET(Constants.END_POINT_GET_WISH_LIST)
    fun getWishList(@Header("Authorization") token: String): Call<ResponseMainWishList>

    @GET(Constants.END_POINT_CHECKOUT_CART)
    fun checkoutCart(@Header("Authorization") token: String): Call<ResponseMainCheckoutCart>

    @GET
    fun placeOrderView(@Header("Authorization") token: String,@Url url : String): Call<ResponseMainPlaceOrderView>

    @GET(Constants.END_POINT_GET_ADDRESS)
    fun getAddress(@Header("Authorization") token: String): Call<ResponseMainAddress>

    @GET(Constants.END_POINT_DELIVERY_OPTION)
    fun getDeliveryOption(): Call<ResponseMainDeliveryOption>

    @GET(Constants.END_POINT_CART)
    fun getCart(@Header("Authorization") token: String): Call<ResponseMainCartNew>

    @DELETE("wish-list/{id}")
    fun deleteWishList(@Header("Authorization") token: String, @Path("id") itemId: Int): Call<ResponseMainLogin>

    @FormUrlEncoded
    @HTTP(method = "DELETE",path = Constants.END_POINT_OUT_OF_STOCK_PRODUCT, hasBody = true)
    fun deleteOutOfStockCart(@Header("Authorization") token: String, @Field("product_ids") product_ids: List<Int>): Call<ResponseMainDeleteCheckoutCart>

    @DELETE("shipping-address/{id}")
    fun deleteAddress(@Header("Authorization") token: String, @Path("id") itemId: Int): Call<ResponseMainLogin>

    @DELETE("cart/{id}")
    fun deleteCart(@Header("Authorization") token: String, @Path("id") itemId: Int): Call<ResponseMainCartNew>

    @FormUrlEncoded
    @PATCH("cart/{id}")
    fun updateCart(
        @Header("Authorization") token: String,
        @Path("id") itemId: Int,
        @Field("quantity") quantity: String
        ): Call<ResponseMainCartNew>

    @FormUrlEncoded
    @POST(Constants.END_POINT_CART)
    fun addToCart(
        @Header("Authorization") token: String,
        @Field("product_id") product_id: String?,
        @Field("quantity") quantity: String?,
        @Field("amount") amount: String?,
    ): Call<ResponseMainLogin>

    @FormUrlEncoded
    @POST(Constants.END_POINT_PLACE_ORDER)
    fun placeOrder(
        @Header("Authorization") token: String,
        @Field("address_id") address_id: String?,
        @Field("delivery_option_id") delivery_option_id: String?,
        @Field("delivery_date") delivery_date: String?,
        @Field("grand_total") grand_total: String?,
        @Field("payment_id") payment_id: String?,
        @Field("payment_method") payment_method: String?,
        @Field("products") products: String,
    ): Call<ResponseMainLogin>


    @FormUrlEncoded
    @POST(Constants.END_POINT_LOGIN)
    fun login(
        @Field("mobile_number") mobile_number: String?,
    ): Call<ResponseMainLogin>

    @FormUrlEncoded
    @POST(Constants.END_POINT_ADD_ADDRESS)
    fun addAddress(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("mobile_number") mobile_number: String,
        @Field("pincode") pincode: String,
        @Field("state") state: String,
        @Field("address") address: String,
        @Field("locality_town") locality_town: String,
        @Field("city") city: String,
        @Field("type") type: String,

        ): Call<ResponseMainLogin>

    @FormUrlEncoded
    @PATCH("shipping-address/{id}")
    fun editAddress(
        @Header("Authorization") token: String,
        @Path("id") itemId: Int,
        @Field("name") name: String,
        @Field("mobile_number") mobile_number: String,
        @Field("pincode") pincode: String,
        @Field("state") state: String,
        @Field("address") address: String,
        @Field("locality_town") locality_town: String,
        @Field("city") city: String,
        @Field("type") type: String,

        ): Call<ResponseMainLogin>

    @POST(Constants.END_POINT_PROFILE)
    @Multipart
    fun addProfile(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("birth_date") birth_date: RequestBody,
        @Part file: MultipartBody.Part,
    ): Call<ResponseMainProfile>

    @POST(Constants.END_POINT_PROFILE)
    @Multipart
    fun addProfileWithoutPhoto(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("birth_date") birth_date: RequestBody,
    ): Call<ResponseMainProfile>


    @FormUrlEncoded
    @POST(Constants.END_POINT_WISH_LIST)
    fun addToWishList(
        @Header("Authorization") token: String,
        @Field("product_id") product_id: String?,
    ): Call<ResponseMainLogin>

    @GET(Constants.END_POINT_FILTER)
    fun getFilter(): Call<ResponseMainFilter>

    @FormUrlEncoded
    @POST(Constants.END_POINT_VERIFY_OTP)
    fun verifyOtp(
        @Field("mobile_number") mobile_number: String?,
        @Field("otp_code") otp_code: String?,
    ): Call<ResponseMainVerifyOtp>

    @FormUrlEncoded
    @POST(Constants.END_POINT_RESEND_OTP)
    fun resendOTP(
        @Field("mobile_number") mobile_number: String?,
    ): Call<ResponseMainLogin>

    @POST(Constants.END_POINT_CART)
    fun addCart(
        @Header("Authorization") token: String?,
        @Body param: JsonArray?
    ): Call<ResponseMainCartNew>

    companion object {
        val cacheSize = (5 * 1024 * 1024).toLong()

        operator fun invoke(mContext: Context): MyApi {

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .callTimeout(20, TimeUnit.SECONDS)
                .build()
            val gson = GsonBuilder().setLenient()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}