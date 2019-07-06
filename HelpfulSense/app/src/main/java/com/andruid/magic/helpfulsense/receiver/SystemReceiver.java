package com.andruid.magic.helpfulsense.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.preference.PreferenceManager;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.service.SensorService;
import com.andruid.magic.helpfulsense.util.IntentUtil;

import timber.log.Timber;

public class SystemReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Intent i = new Intent(context, SensorService.class);
            Timber.d("boot start receiver");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(i);
            else
                context.startService(i);
        }
        else if(Intent.ACTION_BATTERY_LOW.equals(action)) {
            Timber.d("battery low receiver");
            boolean send = PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(context.getString(R.string.pref_low_battery), false);
            if(!send)
                return;
            Intent i = IntentUtil.buildServiceSmsIntent(context,
                    context.getString(R.string.low_battery_message));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(i);
            else
                context.startService(i);
        }
    }
}