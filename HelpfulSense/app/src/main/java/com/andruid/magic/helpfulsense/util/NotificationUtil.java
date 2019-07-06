package com.andruid.magic.helpfulsense.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.activity.MainActivity;
import com.andruid.magic.helpfulsense.service.SensorService;

import static com.andruid.magic.helpfulsense.data.Constants.CHANNEL_ID;
import static com.andruid.magic.helpfulsense.data.Constants.CHANNEL_NAME;
import static com.andruid.magic.helpfulsense.data.Constants.INTENT_LOC_SMS;
import static com.andruid.magic.helpfulsense.data.Constants.INTENT_SERVICE_STOP;
import static com.andruid.magic.helpfulsense.data.Constants.KEY_MESSAGE;

public class NotificationUtil {
    public static NotificationCompat.Builder buildNotification(Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        if(notificationManager==null)
            return null;
        int importance = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent stopIntent = new Intent(context, SensorService.class);
        stopIntent.setAction(INTENT_SERVICE_STOP);
        Intent alertIntent = buildAlertIntent(context);
        return new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setOnlyAlertOnce(true)
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("Safety Service")
                .setContentText("We are ready for your safety!")
                .setShowWhen(true)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop",
                        PendingIntent.getService(context, 0, stopIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT))
                .addAction(R.drawable.ic_message, "Alert",
                        PendingIntent.getService(context, 0, alertIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public static NotificationCompat.Builder buildProgressNotification(Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        if(notificationManager==null)
            return null;
        int importance = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(context, SensorService.class)
                .setAction(INTENT_SERVICE_STOP);
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setOnlyAlertOnce(true)
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("Starting service")
                .setContentText("Please wait while we initialize your sensors and GPS...")
                .setProgress(0, 0, true)
                .setShowWhen(true)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop",
                        PendingIntent.getService(context, 0, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public static Intent buildAlertIntent(Context context){
        return new Intent(context, SensorService.class)
                .setAction(INTENT_LOC_SMS)
                .putExtra(KEY_MESSAGE, context.getString(R.string.alert_msg));
    }
}