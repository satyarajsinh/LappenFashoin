/**
 * Copyright Google Inc. All Rights Reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lapp.service

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lappenfashion.R
import com.lappenfashion.ui.MainActivity
import java.net.HttpURLConnection
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var mNotificationManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null
    private var title: String? = null
    private var body: String? = null
    private var status: String? = null
    private var id: String? = null
    lateinit var mContext: Context

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e(TAG, "Notifications : " + remoteMessage.data.toString())
        val time = System.currentTimeMillis().toString() + ""

        title = remoteMessage.data["title"]
        body = remoteMessage.data["body"]
        status = remoteMessage.data["status"]

        id = time.substring(7, 12)

        mContext = this@MyFirebaseMessagingService
        /*if (isAppInBackground(mContext)) {
            if (MyApplication.activity != null && !title.isNullOrEmpty() && !body.isNullOrEmpty() && !status.isNullOrEmpty()) {
                val handler = Handler(Looper.getMainLooper());
                handler.post(Runnable {
                    Utils.showDialogNotification(MyApplication.activity, title, body, status)
                })
            }
        } else {
            if (MyApplication.activity != null && !title.isNullOrEmpty() && !body.isNullOrEmpty() && !status.isNullOrEmpty()) {
                val handler = Handler(Looper.getMainLooper());
                handler.post(Runnable {
                    Utils.showDialogNotification(MyApplication.activity, title, body, status)
                })
            }
            createNotification(this@MyFirebaseMessagingService, null)
        }*/
        createNotification(this@MyFirebaseMessagingService, null)
    }

    private fun createNotification(mContext: Context, imageUrl: Bitmap?) {
        val resultIntent = Intent(mContext, MainActivity::class.java)
//        resultIntent.putExtra(Keys.IS_FROM_NOTIFICATION, "true")
        resultIntent.putExtra("title", title)
        resultIntent.putExtra("body", body)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        val resultPendingIntent =
            PendingIntent.getActivity(
                mContext,
                id!!.toInt()/* Request code */,
                resultIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )

        mBuilder = NotificationCompat.Builder(mContext)

        if (imageUrl == null) {
            mBuilder!!.setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent)
        } else {
            mBuilder!!.setContentTitle(title)
                .setContentText(body)
                .setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(imageUrl).bigLargeIcon(
                        imageUrl
                    )
                )
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent)
        }

        mNotificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder!!.setSmallIcon(R.drawable.logo)
        } else {
            mBuilder!!.setSmallIcon(R.drawable.logo)
        }

        //        mBuilder.setLargeIcon(bitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                mContext.resources.getString(R.string.app_name),
                importance
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            assert(mNotificationManager != null)
            mBuilder!!.setChannelId(NOTIFICATION_CHANNEL_ID)
            mNotificationManager!!.createNotificationChannel(notificationChannel)
        }
        assert(mNotificationManager != null)
        mNotificationManager!!.notify(Integer.parseInt(id!!), mBuilder!!.build())
    }

    private fun getBitmapFromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1001"
        private const val TAG = "MyFMService"
    }


    private fun isAppInBackground(context: Context): Boolean {
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses =
            activityManager.runningAppProcesses ?: return false
        val packageName = context.packageName
        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == packageName) {
                return true
            }
        }
        return false
    }
}
