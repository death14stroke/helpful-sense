package com.andruid.magic.locationsms.util

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import com.andruid.magic.locationsms.data.*
import com.andruid.magic.locationsms.service.SmsService

fun buildServiceSmsIntent(context: Context, message: String, phoneNumbers: List<String>, className: String,
                          @DrawableRes iconRes: Int): Intent {
    return Intent(context, SmsService::class.java)
            .setAction(ACTION_LOC_SMS)
            .putExtra(EXTRA_MESSAGE, message)
            .putExtra(EXTRA_PHONE_NUMBERS, arrayOf(*phoneNumbers.toTypedArray()))
            .putExtra(EXTRA_CLASS, className)
            .putExtra(EXTRA_ICON_RES, iconRes)
}