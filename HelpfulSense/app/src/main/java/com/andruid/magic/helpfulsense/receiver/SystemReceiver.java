package com.andruid.magic.helpfulsense.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.preference.PreferenceManager;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.service.SensorService;
import com.andruid.magic.helpfulsense.util.SmsUtil;

import timber.log.Timber;

import static com.andruid.magic.helpfulsense.data.Constants.INTENT_LOC_SMS;
import static com.andruid.magic.helpfulsense.data.Constants.KEY_MESSAGE;

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
            Intent i = new Intent(context, SensorService.class);
            i.setAction(INTENT_LOC_SMS);
            i.putExtra(KEY_MESSAGE, "Battery low. ");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(i);
            else
                context.startService(i);
        }
    }
}