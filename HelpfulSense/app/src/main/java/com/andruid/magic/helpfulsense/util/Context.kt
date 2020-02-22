package com.andruid.magic.helpfulsense.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SubscriptionInfo
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.core.content.res.use
import androidx.preference.PreferenceManager
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.database.entity.Contact
import com.andruid.magic.helpfulsense.model.Category
import com.andruid.magic.locationsms.util.checkPhoneStatePermission
import com.andruid.magic.locationsms.util.checkLocationPermission
import com.andruid.magic.locationsms.util.checkSmsPermission
import splitties.systemservices.subscriptionManager

fun Context.hasContactsPermission(): Boolean {
    return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED
}

fun Context.areAllPermissionsGranted(): Boolean {
    return checkLocationPermission() && checkSmsPermission() && checkPhoneStatePermission() && hasContactsPermission()
}

fun Context.isFirstTime() = PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(getString(R.string.pref_first), true)

fun Context.firstTimeDone() {
    PreferenceManager.getDefaultSharedPreferences(this)
            .edit(commit = true) {
                putBoolean(getString(R.string.pref_first), false)
            }
}

fun Context.getCategoryFromRes(): List<Category> {
    val categoryNames = resources.getStringArray(R.array.category_names)
    var typedArray = resources.obtainTypedArray(R.array.category_icons)
    val icons = IntArray(typedArray.length())
    typedArray.use {
        for (i in 0 until typedArray.length())
            icons[i] = typedArray.getResourceId(i, -1)
    }
    typedArray = resources.obtainTypedArray(R.array.category_colors)
    val colors = IntArray(typedArray.length())
    typedArray.use {
        for (i in 0 until typedArray.length())
            colors[i] = typedArray.getResourceId(i, -1)
    }
    val categories = mutableListOf<Category>()
    for (i in icons.indices)
        categories.add(Category(categoryNames[i], icons[i], colors[i]))
    return categories.toList()
}

fun List<Contact>.toPhoneNumbers(): List<String> {
    val numbers = mutableListOf<String>()
    forEach {
        val phoneNumbers = it.phoneNumbers.distinctBy { phoneNumber -> phoneNumber.number }
                .map { phoneNumber -> phoneNumber.number }
        numbers.addAll(phoneNumbers)
    }
    return numbers
}

@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun getSimCards(): List<SubscriptionInfo> = subscriptionManager.activeSubscriptionInfoList