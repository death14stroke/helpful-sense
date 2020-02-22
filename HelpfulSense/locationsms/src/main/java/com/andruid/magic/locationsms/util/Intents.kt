package com.andruid.magic.locationsms.util

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import com.andruid.magic.locationsms.data.*
import com.andruid.magic.locationsms.service.SmsService

/**
 * Build intent to send emergency SMS with current location to trusted contacts
 * @param message text to be sent along with location
 * @param phoneNumbers selected contacts
 * @param className name of the activity to be launched on notification click
 * @param iconRes small icon for notification
 * @return intent to launch [SmsService]
 * @receiver context of the calling component
 */
fun Context.buildServiceSmsIntent(message: String, phoneNumbers: List<String>, className: String,
                                  @DrawableRes iconRes: Int): Intent {
    return Intent(this, SmsService::class.java)
            .setAction(ACTION_LOC_SMS)
            .putExtra(EXTRA_MESSAGE, message)
            .putExtra(EXTRA_PHONE_NUMBERS, arrayOf(*phoneNumbers.toTypedArray()))
            .putExtra(EXTRA_CLASS, className)
            .putExtra(EXTRA_ICON_RES, iconRes)
}