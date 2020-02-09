package com.andruid.magic.helpfulsense.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.andruid.magic.helpfulsense.data.ACTION_LOC_SMS
import com.andruid.magic.helpfulsense.data.KEY_MESSAGE
import com.andruid.magic.helpfulsense.service.SensorService

fun buildSettingsIntent(context: Context): Intent {
    val uri = Uri.fromParts("package", context.packageName, null)
    return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = uri
    }
}

fun buildServiceSmsIntent(context: Context, message: String): Intent {
    return Intent(context, SensorService::class.java).apply {
        action = ACTION_LOC_SMS
        putExtra(KEY_MESSAGE, message)
    }
}