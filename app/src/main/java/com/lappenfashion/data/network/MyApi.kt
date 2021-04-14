package com.lappenfashion.data.network

import android.content.Context
import com.example.simplemvvm.utils.Constants
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lappenfashion.data.model.*
import okhttp3.Cache
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
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
    @GET(Constants.END_POINT_HOME)
    fun getHome(): Call<JsonObject>

    @GET
    fun getProducts(@Url url: String): Call<JsonObject>

    @GET(Constants.END_POINT_CATEGORY)
    fun getCategories(): Call<ResponseMainCategories>

    @GET(Constants.END_POINT_GET_WISH_LIST)
    fun getWishList(@Header("Authorization") token: String): Call<ResponseMainWishList>

    @GET(Constants.END_POINT_GET_ADDRESS)
    fun getAddress(@Header("Authorization") token: String): Call<ResponseMainAddress>

    @GET(Constants.END_POINT_CART)
    fun getCart(@Header("Authorization") token: String): Call<ResponseMainCartNew>

    @DELETE("wish-list/{id}")
    fun deleteWishList(@Header("Authorization") token: String, @Path("id") itemId: Int): Call<ResponseMainLogin>

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

    @Multipart
    @POST(Constants.END_POINT_PROFILE)
    fun addProfile(
        @Header("Authorization") token: String,
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("gender") gender: String,
        @Query("birth_date") birth_date: String,
        @Part file: MultipartBody.Part,
    ): Call<ResponseMainProfile>


    @FormUrlEncoded
    @POST(Constants.END_POINT_WISH_LIST)
    fun addToWishList(
        @Header("Authorization") token: String,
        @Field("product_id") product_id: String?,
    ): Call<ResponseMainLogin>

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
        @Header("access-token") token: String?,
        @Body param: JsonArray?
    ): Call<ResponseMainCartNew>

    companion object {
        val cacheSize = (5 * 1024 * 1024).toLong()

        operator fun invoke(mContext: Context): MyApi {
            val myCache = Cache(mContext.cacheDir, cacheSize)

            val okHttpClient = OkHttpClient.Builder()
                .cache(myCache)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .callTimeout(20, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    var request = chain.request()
                    request = if (NetworkConnection.checkConnection(mContext)!!)
                        request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                    else
                        request.newBuilder().header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                        ).build()
                    chain.proceed(request)
                }
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_MAIN)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}