package com.andruid.magic.helpfulsense.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.service.SensorService
import com.andruid.magic.helpfulsense.util.buildServiceSmsIntent
import com.andruid.magic.helpfulsense.util.startFgOrBgService
import timber.log.Timber

class SystemReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                val i = Intent(context, SensorService::class.java)
                Timber.d("boot start receiver")
                context.startFgOrBgService(i)
            }
            Intent.ACTION_BATTERY_LOW -> {
                Timber.d("battery low receiver")
                val send = PreferenceManager.getDefaultSharedPreferences(context)
                        .getBoolean(context.getString(R.string.pref_low_battery), false)
                if (send) {
                    val i = buildServiceSmsIntent(context, context.getString(R.string.low_battery_message))
                    context.startFgOrBgService(i)
                }
            }
        }
    }
}