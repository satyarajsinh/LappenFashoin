package com.lappenfashion.data.network

import com.example.simplemvvm.utils.Constants
import com.google.gson.JsonObject
import com.lappenfashion.data.model.ResponseMain
import com.lappenfashion.data.model.ResponseMainCategories
import com.lappenfashion.data.model.ResponseMainHome
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import java.util.concurrent.TimeUnit

interface MyApi {

    /*@GET(Constants.END_POINT)
    fun getCategories(): Call<ResponseMain>
*/
    @GET(Constants.END_POINT_HOME)
    fun getHome(): Call<ResponseMainHome>

    @GET(Constants.END_POINT_CATEGORY)
    fun getCategories(): Call<ResponseMainCategories>

    companion object {
        operator fun invoke(): MyApi {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .callTimeout(20, TimeUnit.SECONDS)
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