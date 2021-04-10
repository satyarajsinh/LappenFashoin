package com.lappenfashion;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks{

    public static Activity activity;
    private Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        // Initialize the Prefs class
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        MyApplication.activity = activity;
        Log.e("Activity Created", activity.getLocalClassName());

    }

    @Override
    public void onActivityStarted(Activity activity) {
        MyApplication.activity = activity;
        Log.d("Activity Started", activity.getLocalClassName());

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d("Activity Resumed", activity.getLocalClassName());

    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d("Activity Paused", activity.getLocalClassName());

    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d("Activity Stopped", activity.getLocalClassName());

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//        Log.d("Activity SaveInstanceState", activity.getLocalClassName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d("Activity Destroyed", activity.getLocalClassName());
    }
}