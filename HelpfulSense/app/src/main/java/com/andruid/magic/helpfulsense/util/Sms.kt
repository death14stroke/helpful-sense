package com.andruid.magic.helpfulsense.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import com.andruid.magic.helpfulsense.data.ACTION_SMS_SENT
import com.andruid.magic.helpfulsense.database.DbRepository
import com.andruid.magic.helpfulsense.service.SensorService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

fun Location.getMapsUrl() = "http://maps.google.com/?q=${latitude},$longitude"

suspend fun Context.sendSMS(location: Location, msg: String) {
    val message = "$msg ${location.getMapsUrl()}"
    val smsManager = SmsManager.getDefault()
    val parts = smsManager.divideMessage(message)
    val intent = Intent(this, SensorService::class.java).apply {
        action = ACTION_SMS_SENT
    }
    val sentIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        PendingIntent.getForegroundService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    else
        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    Timber.d("before sending sms")
    val contacts = withContext(Dispatchers.IO) { DbRepository.getInstance().fetchContacts() }
    for (contact in contacts) {
        Timber.d("${contact.name} : ${contact.phoneNumbers.size} numbers")
        for (phone in contact.phoneNumbers.distinctBy { phoneNumber -> phoneNumber.number }) {
            Timber.d("sendSMS: ${contact.name} : ${phone.number}")
            //TODO: uncomment to send actual sms
            /*smsManager.sendMultipartTextMessage(phone.number, null, parts,
                    arrayListOf<PendingIntent>(sentIntent), null)*/
        }
    }
}