package com.lappenfashion.data.network

import android.content.Context
import com.example.simplemvvm.utils.Constants
import com.google.gson.JsonObject
import com.lappenfashion.data.model.ResponseMainCategories
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface MyApi {

    /*@GET(Constants.END_POINT)
    fun getCategories(): Call<ResponseMain>
*/
    @GET(Constants.END_POINT_HOME)
    fun getHome(): Call<JsonObject>

    @GET(Constants.END_POINT_CATEGORY)
    fun getCategories(): Call<ResponseMainCategories>

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
                        request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
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