package com.lapp.service

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

public class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        var refreshedToken = FirebaseInstanceId.getInstance().token
        Log.e("onTokenRefresh","onTokenRefresh"+refreshedToken)
        super.onTokenRefresh()
    }
}