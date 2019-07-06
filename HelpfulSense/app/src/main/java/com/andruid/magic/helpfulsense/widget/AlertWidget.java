package com.andruid.magic.helpfulsense.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.service.SensorService;
import com.andruid.magic.helpfulsense.util.NotificationUtil;

import static com.andruid.magic.helpfulsense.data.Constants.INTENT_LOC_SMS;
import static com.andruid.magic.helpfulsense.data.Constants.KEY_MESSAGE;

public class AlertWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.alert_widget);
        Intent serIntent = new Intent(context, SensorService.class);
        views.setOnClickPendingIntent(R.id.serviceBtn, PendingIntent.getService(context, 0,
                serIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        Intent alertIntent = NotificationUtil.buildAlertIntent(context);
        views.setOnClickPendingIntent(R.id.alertBtn, PendingIntent.getService(context, 0,
                alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds)
            updateAppWidget(context, appWidgetManager, appWidgetId);
    }

    @Override
    public void onEnabled(Context context) {}

    @Override
    public void onDisabled(Context context) {}
}

