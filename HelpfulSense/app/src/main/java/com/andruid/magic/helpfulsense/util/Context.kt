package com.andruid.magic.helpfulsense.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SubscriptionInfo
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.content.res.use
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.database.entity.Contact
import com.andruid.magic.helpfulsense.model.Category
import com.andruid.magic.locationsms.util.hasLocationPermission
import com.andruid.magic.locationsms.util.checkPhoneStatePermission
import com.andruid.magic.locationsms.util.hasSmsPermission
import splitties.systemservices.subscriptionManager

fun Context.color(color: Int) =
        ContextCompat.getColor(this, color)

fun Context.drawable(res: Int) =
        ContextCompat.getDrawable(this, res)

fun Context.hasContactsPermission(): Boolean {
    return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED
}

fun Context.areAllPermissionsGranted(): Boolean {
    return hasLocationPermission() && hasSmsPermission() && checkPhoneStatePermission() && hasContactsPermission()
}

fun Context.isFirstTime() = PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(getString(R.string.pref_first), true)

fun Context.firstTimeDone() {
    PreferenceManager.getDefaultSharedPreferences(this)
            .edit(commit = true) {
                putBoolean(getString(R.string.pref_first), false)
            }
}

fun Context.startFgOrBgService(intent: Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        startForegroundService(intent)
    else
        startService(intent)
}

fun Fragment.startFgOrBgService(intent: Intent) {
    requireContext().startFgOrBgService(intent)
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

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
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