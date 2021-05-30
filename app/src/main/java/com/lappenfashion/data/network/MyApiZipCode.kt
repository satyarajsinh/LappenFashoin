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

interface MyApiZipCode {

    @GET
    fun checkPincode(@Url url: String): Call<ResponseMainZipCode>

    companion object {
        val cacheSize = (5 * 1024 * 1024).toLong()

        operator fun invoke(mContext: Context): MyApiZipCode {

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .callTimeout(20, TimeUnit.SECONDS)
                .build()
            val gson = GsonBuilder().setLenient()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_ZIP_CODE)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MyApiZipCode::class.java)
        }
    }
}