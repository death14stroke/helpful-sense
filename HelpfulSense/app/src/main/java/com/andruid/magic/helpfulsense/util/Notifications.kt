package com.andruid.magic.helpfulsense.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.data.ACTION_STOP_SERVICE
import com.andruid.magic.helpfulsense.service.SensorService
import com.andruid.magic.helpfulsense.ui.activity.MainActivity

private const val CHANNEL_ID = "channel_sensor"
private const val CHANNEL_NAME = "Sensor Service"

fun Context.buildNotification(): NotificationCompat.Builder {
    val notificationManager = getSystemService<NotificationManager>()!!
    val importance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        NotificationManager.IMPORTANCE_HIGH
    else {
        0
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            enableLights(true)
            lightColor = Color.RED
        }
        notificationManager.createNotificationChannel(notificationChannel)
    }
    val stopIntent = Intent(this, SensorService::class.java).apply {
        action = ACTION_STOP_SERVICE
    }
    val alertIntent = buildServiceSmsIntent(this, getString(R.string.alert_msg))
    val clickIntent = Intent(this@buildNotification, MainActivity::class.java)
    return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setOnlyAlertOnce(true)
            .setContentIntent(PendingIntent.getActivity(this, 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT))
            .setContentTitle(getString(R.string.safety_service_title))
            .setContentText(getString(R.string.safety_service_text))
            .setShowWhen(true)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, getString(R.string.stop),
                    PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT))
            .addAction(R.drawable.ic_message, getString(R.string.alert),
                    PendingIntent.getService(this, 0, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT))
}

fun Context.buildProgressNotification(): NotificationCompat.Builder {
    val notificationManager = getSystemService<NotificationManager>()!!
    val importance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        NotificationManager.IMPORTANCE_HIGH
    else {
        0
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            enableLights(true)
            lightColor = Color.RED
        }
        notificationManager.createNotificationChannel(notificationChannel)
    }
    val intent = Intent(this, SensorService::class.java).apply {
        action = ACTION_STOP_SERVICE
    }
    val clickIntent = Intent(this, MainActivity::class.java)
    return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setOnlyAlertOnce(true)
            .setContentIntent(PendingIntent.getActivity(this, 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT))
            .setContentTitle(getString(R.string.progress_noti_title))
            .setContentText(getString(R.string.progress_noti_text))
            .setProgress(0, 0, true)
            .setShowWhen(true)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, getString(R.string.stop),
                    PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
}