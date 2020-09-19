package com.andruid.magic.helpfulsense.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import com.andruid.magic.eezetensions.startFgOrBgService
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.database.DbRepository
import com.andruid.magic.helpfulsense.database.entity.toPhoneNumbers
import com.andruid.magic.helpfulsense.ui.activity.HomeActivity
import com.andruid.magic.helpfulsense.util.areAllPermissionsGranted
import com.andruid.magic.helpfulsense.util.isFirstTime
import com.andruid.magic.locationsms.data.ACTION_START_SERVICE
import com.andruid.magic.locationsms.data.EXTRA_PHONE_NUMBERS
import com.andruid.magic.locationsms.service.SmsService
import com.andruid.magic.locationsms.util.buildServiceSmsIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Broadcast receiver for [Intent.ACTION_BOOT_COMPLETED], [Intent.ACTION_BATTERY_LOW]
 */
class SystemReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                Timber.i("onReceive: boot start receiver")
                GlobalScope.launch {
                    DbRepository.fetchAllContacts()
                            .flowOn(Dispatchers.Default)
                            .collect { contacts ->
                                val phoneNumbers = contacts.toPhoneNumbers()
                                val i = Intent(context, SmsService::class.java)
                                        .setAction(ACTION_START_SERVICE)
                                        .putExtra(EXTRA_PHONE_NUMBERS, arrayOf(*phoneNumbers.toTypedArray()))
                                if (!context.isFirstTime() && context.areAllPermissionsGranted())
                                    context.startFgOrBgService(i)
                            }
                }
            }
            Intent.ACTION_BATTERY_LOW -> {
                Timber.i("onReceive: battery low receiver")
                val send = PreferenceManager.getDefaultSharedPreferences(context)
                        .getBoolean(context.getString(R.string.pref_low_battery), false)
                if (send && !context.isFirstTime()) {
                    GlobalScope.launch {
                        DbRepository.fetchAllContacts()
                                .flowOn(Dispatchers.Default)
                                .collect { contacts ->
                                    val phoneNumbers = contacts.toPhoneNumbers()
                                    val className = HomeActivity::class.java.name
                                    val i = context.buildServiceSmsIntent(context.getString(R.string.low_battery_message), phoneNumbers, className, R.mipmap.ic_launcher)
                                    context.startFgOrBgService(i)
                                }
                    }
                }
            }
        }
    }
}