package com.andruid.magic.locationsms.util

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.telephony.SmsManager
import androidx.annotation.RequiresPermission
import com.andruid.magic.locationsms.data.ACTION_SMS_SENT
import com.andruid.magic.locationsms.service.SmsService
import splitties.systemservices.subscriptionManager
import timber.log.Timber

fun Location.getMapsUrl() = "http://maps.google.com/?q=${latitude},$longitude"

@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Context.sendSMS(location: Location, msg: String, phoneNumbers: List<String>) {
    val message = "$msg ${location.getMapsUrl()}"
    val simSlot = getSelectedSimSlot()
    val subscriptionId = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simSlot).subscriptionId
    val smsManager = SmsManager.getSmsManagerForSubscriptionId(subscriptionId)
    val parts = smsManager.divideMessage(message)
    val intent = Intent(this, SmsService::class.java)
            .setAction(ACTION_SMS_SENT)
    val sentIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        PendingIntent.getForegroundService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    else
        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    Timber.i("sendSMS: before sending sms")
    for (phone in phoneNumbers) {
        Timber.d("sendSMS: $phone")
        //TODO: uncomment to send actual sms
        /*smsManager.sendMultipartTextMessage(phone, null, parts,
                arrayListOf<PendingIntent>(sentIntent), null)*/
    }
}