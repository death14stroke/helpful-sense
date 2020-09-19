package com.andruid.magic.locationsms.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

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

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    requireContext().toast(message, duration)
}

fun Context.toast(@StringRes messageRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(messageRes), duration)
}