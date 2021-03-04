package com.lappenfashion.data.network

import android.content.Context
import android.net.ConnectivityManager

object NetworkConnection {

    fun checkConnection(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)as ConnectivityManager

        if(connectivityManager!=null){
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if(activeNetworkInfo!=null){
                return if(activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI){
                    true
                }else{
                    activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE
                }
            }
        }
        return false
    }
}