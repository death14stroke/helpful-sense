package com.andruid.magic.helpfulsense.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.andruid.magic.locationsms.util.checkLocationPermission
import com.andruid.magic.locationsms.util.checkPhoneStatePermission
import com.andruid.magic.locationsms.util.checkSmsPermission

/**
 * Check if the [Manifest.permission.READ_CONTACTS] permission is granted
 * @return true/false
 * @receiver context of the calling component
 */
fun Context.checkContactsPermission(): Boolean {
    return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED
}

/**
 * Check if all the permissions: [Manifest.permission.ACCESS_FINE_LOCATION], [Manifest.permission.SEND_SMS],
 * [Manifest.permission.READ_PHONE_STATE] and [Manifest.permission.READ_CONTACTS] are granted
 * @return true/false
 * @receiver context of the calling component
 */
fun Context.areAllPermissionsGranted(): Boolean {
    return checkLocationPermission() && checkSmsPermission() && checkPhoneStatePermission() && checkContactsPermission()
}