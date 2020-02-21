package com.andruid.magic.helpfulsense.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun buildSettingsIntent(context: Context): Intent {
    val uri = Uri.fromParts("package", context.packageName, null)
    return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = uri
    }
}