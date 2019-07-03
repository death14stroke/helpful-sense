package com.andruid.magic.helpfulsense.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

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
        else if(Intent.ACTION_SHUTDOWN.equals(action)) {
            Timber.d("shutdown receiver");
            SmsUtil.sendBootSms(context, "Phone shutdown.");
        }
        else if(Intent.ACTION_BATTERY_LOW.equals(action)) {
            Timber.d("battery low receiver");
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