package com.andruid.magic.helpfulsense.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class IntentUtil {
    public static Intent buildSettingsIntent(Context context){
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(uri);
    }
}