package com.lappenfashion.service;

import android.app.Service;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

public class MessageService extends FirebaseMessagingService {

    String TAG = "Service";
    private NotificationUtils notificationUtils = NotificationUtils.getInstance();

    public void onMessageReceived(RemoteMessage p0) {
        Log.e(TAG, "Notification Message Body: " + p0.getData());
        Log.e(TAG, "Notification Message notification: " + p0.getNotification());

        if (p0.getData() != null && p0.getData().size()>0) {
            Map<String, String> data = p0.getData();
            String body = data.get("body");
            String title = data.get("title");
            notificationUtils.generateNotification(this,body,title);
        }else {
            String body = p0.getNotification().getBody();
            String title = p0.getNotification().getTitle();
            notificationUtils.generateNotification(this,body,title);
        }
    }

    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        sendRegistrationToServer(refreshedToken);
        Log.e("FirebaseInstanceService", "refreshedToken : $refreshedToken");
    }

    public void sendRegistrationToServer(String token) { // sending gcm token to server
        Log.e(
                TAG,
                "sendRegistrationToServer: $token"
        );
    }

}
