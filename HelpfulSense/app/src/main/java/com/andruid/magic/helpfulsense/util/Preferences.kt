package com.andruid.magic.helpfulsense.util

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.andruid.magic.helpfulsense.R

/**
 * Check if app is being launched first time or not
 * @return true/false
 * @receiver context of the calling component
 */
fun Context.isFirstTime() = PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(getString(R.string.pref_first), true)

/**
 * Mark the user has seen the intro first time
 * @receiver context of the calling component
 */
fun Context.firstTimeDone() {
    PreferenceManager.getDefaultSharedPreferences(this)
            .edit(commit = true) {
                putBoolean(getString(R.string.pref_first), false)
            }
}