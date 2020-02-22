package com.andruid.magic.locationsms.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

/**
 * Check if the [Manifest.permission.ACCESS_FINE_LOCATION] permission is granted
 * @return true/false
 * @receiver context of the calling component
 */
fun Context.checkLocationPermission(): Boolean {
    return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
}

/**
 * Check if the [Manifest.permission.SEND_SMS] permission is granted
 * @return true/false
 * @receiver context of the calling component
 */
fun Context.checkSmsPermission(): Boolean {
    return ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) ==
            PackageManager.PERMISSION_GRANTED
}

/**
 * Check if the [Manifest.permission.READ_PHONE_STATE] permission is granted
 * @return true/false
 * @receiver context of the calling component
 */
fun Context.checkPhoneStatePermission(): Boolean {
    return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) ==
            PackageManager.PERMISSION_GRANTED
}