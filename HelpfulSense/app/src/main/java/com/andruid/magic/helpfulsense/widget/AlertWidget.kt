package com.andruid.magic.helpfulsense.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.database.DbRepository
import com.andruid.magic.helpfulsense.database.entity.toPhoneNumbers
import com.andruid.magic.helpfulsense.ui.activity.HomeActivity
import com.andruid.magic.locationsms.service.SmsService
import com.andruid.magic.locationsms.util.buildServiceSmsIntent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Widget to send alert, start [SmsService] for listening shake events and more
 */
class AlertWidget : AppWidgetProvider() {
    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val serIntent = Intent(context, SmsService::class.java)
            GlobalScope.launch {
                DbRepository.fetchAllContacts()
                        .map { contacts -> contacts.toPhoneNumbers() }
                        .collect { phoneNumbers ->
                            val alertIntent = context.buildServiceSmsIntent(context.getString(R.string.alert_msg), phoneNumbers,
                                    HomeActivity::class.java.name, R.mipmap.ic_launcher)
                            val views = RemoteViews(context.packageName, R.layout.alert_widget).apply {
                                setOnClickPendingIntent(R.id.serviceBtn, PendingIntent.getService(context, 0,
                                        serIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                                setOnClickPendingIntent(R.id.alertBtn, PendingIntent.getService(context, 0,
                                        alertIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                                setOnClickPendingIntent(R.id.activityBtn, PendingIntent.getActivity(context, 0,
                                        Intent(context, HomeActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
                            }
                            appWidgetManager.updateAppWidget(appWidgetId, views)
                        }
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (appWidgetId in appWidgetIds)
            updateAppWidget(context, appWidgetManager, appWidgetId)
    }
}