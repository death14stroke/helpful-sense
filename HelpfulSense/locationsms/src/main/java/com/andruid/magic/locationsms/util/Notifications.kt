package com.andruid.magic.locationsms.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.andruid.magic.locationsms.R
import com.andruid.magic.locationsms.data.ACTION_STOP_SERVICE
import com.andruid.magic.locationsms.service.SmsService
import splitties.systemservices.notificationManager
import timber.log.Timber

// notification channel id for sensor service
private const val CHANNEL_ID = "channel_sensor"
// notification channel name for sensor service
private const val CHANNEL_NAME = "Sensor Service"

/**
 * Build [NotificationCompat.Builder] for running [SmsService] in foreground
 * @param phoneNumbers selected phoneNumbers
 * @param className name of the activity to be launched on notification click
 * @param icon small icon for notification
 * @return notification builder
 * @receiver context of the calling component
 */
fun Context.buildNotification(phoneNumbers: List<String>, className: String, @DrawableRes icon: Int)
        : NotificationCompat.Builder {
    val importance = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> NotificationManager.IMPORTANCE_LOW
        else -> 0
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            enableLights(true)
            lightColor = Color.RED
        }
        notificationManager.createNotificationChannel(notificationChannel)
    }

    val stopIntent = Intent(this, SmsService::class.java)
            .setAction(ACTION_STOP_SERVICE)
    val alertIntent = buildServiceSmsIntent(getString(R.string.alert_msg), phoneNumbers, className, icon)
    val clazz = Class.forName(className)
    Timber.tag("notiLog").d("className before = $className, after = ${clazz.canonicalName}")
    val clickIntent = Intent(this, clazz)

    return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(icon)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setOnlyAlertOnce(true)
            .setContentIntent(PendingIntent.getActivity(this, 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT))
            .setContentTitle(getString(R.string.safety_service_title))
            .setContentText(getString(R.string.safety_service_text))
            .setShowWhen(true)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, getString(R.string.stop),
                    PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT))
            .addAction(android.R.drawable.ic_dialog_email, getString(R.string.alert),
                    PendingIntent.getService(this, 0, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT))
}

/**
 * Build [NotificationCompat.Builder] for running [SmsService] while it is initializing
 * @param icon small icon for notification
 * @return notification builder
 * @receiver context of the calling component
 */
fun Context.buildProgressNotification(@DrawableRes icon: Int): NotificationCompat.Builder {
    val importance = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> NotificationManager.IMPORTANCE_HIGH
        else -> 0
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            enableLights(true)
            lightColor = Color.RED
        }
        notificationManager.createNotificationChannel(notificationChannel)
    }

    val intent = Intent(this, SmsService::class.java)
            .setAction(ACTION_STOP_SERVICE)
    return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setOnlyAlertOnce(true)
            .setContentTitle(getString(R.string.progress_noti_title))
            .setContentText(getString(R.string.progress_noti_text))
            .setProgress(0, 0, true)
            .setShowWhen(true)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, getString(R.string.stop),
                    PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
}