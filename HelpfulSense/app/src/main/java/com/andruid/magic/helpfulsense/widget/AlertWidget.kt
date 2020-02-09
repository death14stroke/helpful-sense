package com.andruid.magic.helpfulsense.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.service.SensorService
import com.andruid.magic.helpfulsense.ui.activity.MainActivity
import com.andruid.magic.helpfulsense.util.buildServiceSmsIntent

class AlertWidget : AppWidgetProvider() {
    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val serIntent = Intent(context, SensorService::class.java)
            val alertIntent = buildServiceSmsIntent(context, context.getString(R.string.alert_msg))
            val views = RemoteViews(context.packageName, R.layout.alert_widget).apply {
                setOnClickPendingIntent(R.id.serviceBtn, PendingIntent.getService(context, 0,
                        serIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                setOnClickPendingIntent(R.id.alertBtn, PendingIntent.getService(context, 0,
                        alertIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                setOnClickPendingIntent(R.id.activityBtn, PendingIntent.getActivity(context, 0,
                        Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (appWidgetId in requireNotNull(appWidgetIds, { "App Widget IDs is null" }))
            updateAppWidget(requireNotNull(context), requireNotNull(appWidgetManager), appWidgetId)
    }
}