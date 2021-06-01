package com.lappenfashion.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.lappenfashion.R;
import com.lappenfashion.ui.MainActivity;

public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();


    private Context mContext;
    private Bitmap bitmap;
    private PendingIntent resultPendingIntent;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public static NotificationUtils mInstance = new NotificationUtils();

    public NotificationUtils() {
    }

    public static NotificationUtils getInstance() {
        return mInstance;
    }

    public void generateNotification(Context context, String body, String title) {

        if (context == null)
            return;

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            NotificationChannel mChannel;
            String CHANNEL_ID = "";
            CharSequence name = "";
            String Description = "";

            // If isSilent flag is true, does not make sound and vibrate.
            CHANNEL_ID = "my_channel_02";
            name = "Brother Heart Shayar Data";
            Description = "Brother Heart Shayar Data";

//                builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
//                builder.build().defaults |= Notification.DEFAULT_SOUND;
//                builder.build().defaults |= Notification.DEFAULT_VIBRATE;

            int importance = NotificationManager.IMPORTANCE_HIGH;
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableVibration(false);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("" + title)
                    .setContentText("" + body)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("" + body))
                    .setSmallIcon(R.mipmap.app_icon)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true)
                    .setSound(null)
                    .setDefaults(0);


            if (bitmap != null) {
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(body));
            }
//                    .addAction(R.drawable.ic_launcher_foreground, "Call", resultPendingIntent)
//                    .addAction(R.drawable.ic_launcher_foreground, "More", resultPendingIntent)
//                    .addAction(R.drawable.ic_launcher_foreground, "And more", resultPendingIntent);

            try {
                Intent resultIntent = new Intent(context, MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

                stackBuilder.addParentStack(MainActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(),
                        resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(resultPendingIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }


            if (mNotificationManager != null) {
                mNotificationManager.notify((int) System.currentTimeMillis(), builder.build());
            }

            bitmap = null;

        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {

            Bitmap notificationLargeIconBitmap = BitmapFactory.decodeResource(
                    context.getResources(), R.mipmap.ic_launcher);

            mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                    context).setAutoCancel(true)
//                    .setContentTitle("" + context.getResources().getString(R.string.app_name))
                    .setContentTitle(" " + title)
                    .setContentText(body)
                    .setColor(context.getResources().getColor(R.color.white))
                    .setSmallIcon(R.mipmap.ic_launcher);
//                    .setLargeIcon(notificationLargeIconBitmap)
            if (bitmap != null) {
                mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
            }

            // Required for heads-up notification
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setPriority(Notification.PRIORITY_HIGH);
                mBuilder.setAutoCancel(true);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mBuilder.setSmallIcon(R.mipmap.app_icon);
//                        .setLargeIcon(notificationLargeIconBitmap);
            }

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                mBuilder.setSmallIcon(R.mipmap.app_icon);
//                mBuilder.setLargeIcon(notificationLargeIconBitmap);
            }

            Intent resultIntent = null;
            try {
                resultIntent = new Intent(context, MainActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(),
                        resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // If isSilent flag is true, does not make sound and vibrate.

            mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);

            Notification notification = mBuilder.build();

            mBuilder.setPublicVersion(notification);


            // Notification id allows you to updateFragment the notification later on.
            if (mNotificationManager != null) {
                mNotificationManager.notify((int) System.currentTimeMillis(), notification);
            }

            bitmap = null;
        } else {
            mBuilder = new NotificationCompat.Builder(context);

            Bitmap notificationLargeIconBitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.mipmap.app_icon);
            mBuilder.setContentTitle("" + title);
            mBuilder.setContentText("" + body);
            mBuilder.setColor(context.getResources().getColor(R.color.white));
            mBuilder.setLargeIcon(notificationLargeIconBitmap);

            if (bitmap != null) {
                mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
            }

//            //Creates an explicit intent for an Activity in your app
            Intent resultIntent = null;
            try {
                resultIntent = new Intent(context, MainActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(),
                        resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Notification notification = mBuilder.getNotification();

            mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            //Notification id allows you to updateFragment the notification later on.
            if (mNotificationManager != null) {
                mNotificationManager.notify((int) System.currentTimeMillis(), notification);
            }
        }
    }

    private void setNofication(Context context, String screen, String value, boolean isSilent, NotificationCompat.Builder mBuilder) {
    }

    public void removeNotification(Context context) {

        // If unread messages contains any message from this sender, just remove from hashmap
        NotificationManager nMgr = (NotificationManager) context.getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(10);
    }

    public void removeAllNotifications(Context context) {

        if (context != null) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.cancelAll();
            }
        }
    }

}