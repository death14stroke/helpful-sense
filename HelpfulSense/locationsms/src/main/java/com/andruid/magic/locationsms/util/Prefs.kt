package com.andruid.magic.locationsms.util

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.andruid.magic.locationsms.R

/**
 * Get shake threshold from preferences
 * @return shake threshold
 * @receiver context of the calling component
 */
fun Context.getShakeThreshold(): Int {
    return PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_threshold),
            resources.getInteger(R.integer.def_threshold).toString())!!.toInt()
}

/**
 * Get shake threshold from preferences
 * @return shake threshold
 * @receiver active fragment
 */
fun Fragment.getShakeThreshold() = requireContext().getShakeThreshold()

/**
 * Get shake stop time from preferences
 * @return shake stop time
 * @receiver context of the calling component
 */
fun Context.getShakeStopTime(): Int {
    return PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_time_stop),
            resources.getInteger(R.integer.def_time_stop).toString())!!.toInt()
}

/**
 * Get shake stop time from preferences
 * @return shake stop time
 * @receiver active fragment
 */
fun Fragment.getShakeStopTime() = requireContext().getShakeStopTime()

/**
 * Get SIM slot number selected for sending SMS
 * @return SIM slot number
 * @receiver context of the calling component
 */
fun Context.getSelectedSimSlot(): Int {
    return PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_sms_sim),
            getDefaultSmsSimSlot())!!.toInt()
}

/**
 * Get SIM slot number selected for sending SMS
 * @return SIM slot number
 * @receiver active fragment
 */
fun Fragment.getSelectedSimSlot() = requireContext().getSelectedSimSlot()

/**
 * Get default SIM slot number for sending SMS (SIM 1)
 * @return default SIM slot number
 */
fun getDefaultSmsSimSlot() = "1"