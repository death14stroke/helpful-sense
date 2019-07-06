package com.andruid.magic.helpfulsense.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.andruid.magic.helpfulsense.service.SensorService;

import static com.andruid.magic.helpfulsense.data.Constants.INTENT_LOC_SMS;
import static com.andruid.magic.helpfulsense.data.Constants.KEY_MESSAGE;

public class IntentUtil {
    static Intent buildSettingsIntent(Context context){
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(uri);
    }

    public static Intent buildServiceSmsIntent(Context context, String message){
        return new Intent(context, SensorService.class)
                .setAction(INTENT_LOC_SMS)
                .putExtra(KEY_MESSAGE, message);
    }
}