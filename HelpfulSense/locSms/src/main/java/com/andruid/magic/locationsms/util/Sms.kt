package com.andruid.magic.locationsms.util

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import com.andruid.magic.locationsms.data.ACTION_SMS_SENT
import com.andruid.magic.locationsms.service.SmsService
import splitties.systemservices.subscriptionManager
import timber.log.Timber

fun Location.getMapsUrl() = "http://maps.google.com/?q=${latitude},$longitude"

fun Context.sendSMS(location: Location, msg: String, phoneNumbers: List<String>) {
    val message = "$msg ${location.getMapsUrl()}"
    val smsManager = SmsManager.getDefault()
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
         if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
             // TODO: Consider calling
             //    ActivityCompat#requestPermissions
             // here to request the missing permissions, and then overriding
             //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
             //                                          int[] grantResults)
             // to handle the case where the user grants the permission. See the documentation
             // for ActivityCompat#requestPermissions for more details.
             return
         }
         //TODO: send sms using selected sim
         //SmsManager.getSmsManagerForSubscriptionId().sendMultipartTextMessage()
         subscriptionManager.activeSubscriptionInfoList.forEach {
             Timber.d("sendSMS: sub info: ${it.carrierName} : ${it.simSlotIndex}")
         }
        //TODO: uncomment to send actual sms
        /*smsManager.sendMultipartTextMessage(phone, null, parts,
                arrayListOf<PendingIntent>(sentIntent), null)*/
    }
}