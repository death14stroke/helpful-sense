package com.andruid.magic.locationsms.util

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.andruid.magic.locationsms.R

fun Context.getShakeThreshold(): Int {
    return PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_threshold),
            resources.getInteger(R.integer.def_threshold).toString())!!.toInt()
}

fun Fragment.getShakeThreshold() = requireContext().getShakeThreshold()

fun Context.getShakeStopTime(): Int {
    return PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_time_stop),
            resources.getInteger(R.integer.def_time_stop).toString())!!.toInt()
}

fun Fragment.getShakeStopTime() = requireContext().getShakeStopTime()

fun Context.getSelectedSimSlot(): Int {
    return PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_sms_sim),
            getDefaultSmsSimSlot())!!.toInt()
}

fun Fragment.getSelectedSimSlot() = requireContext().getSelectedSimSlot()

fun getDefaultSmsSimSlot() = "1"