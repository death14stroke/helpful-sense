package com.andruid.magic.helpfulsense.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.andruid.magic.eezetensions.color
import com.andruid.magic.eezetensions.startFgOrBgService
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.data.ACTION_SHORTCUT_LAUNCH
import com.andruid.magic.helpfulsense.data.CONTACTS_PICKER_REQUEST
import com.andruid.magic.helpfulsense.data.EXTRA_SHORTCUT_MESSAGE
import com.andruid.magic.helpfulsense.database.DbRepository
import com.andruid.magic.helpfulsense.database.entity.toContact
import com.andruid.magic.helpfulsense.database.entity.toPhoneNumbers
import com.andruid.magic.helpfulsense.databinding.ActivityHomeBinding
import com.andruid.magic.helpfulsense.eventbus.ContactsEvent
import com.andruid.magic.helpfulsense.ui.util.buildInfoDialog
import com.andruid.magic.helpfulsense.ui.util.buildSettingsDialog
import com.andruid.magic.helpfulsense.ui.viewbinding.viewBinding
import com.andruid.magic.locationsms.data.ACTION_START_SERVICE
import com.andruid.magic.locationsms.data.EXTRA_CLASS
import com.andruid.magic.locationsms.data.EXTRA_ICON_RES
import com.andruid.magic.locationsms.data.EXTRA_PHONE_NUMBERS
import com.andruid.magic.locationsms.service.SmsService
import com.andruid.magic.locationsms.util.buildServiceSmsIntent
import com.andruid.magic.locationsms.util.toast
import com.wafflecopter.multicontactpicker.MultiContactPicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import permissions.dispatcher.*
import splitties.systemservices.shortcutManager

/**
 * Home page activity of the application
 */
@RuntimePermissions
class HomeActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityHomeBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.apply {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                bottomNavView.setBackgroundColor(color(when (destination.id) {
                    R.id.alertFragment -> R.color.colorTab1
                    R.id.messageFragment -> R.color.colorTab2
                    R.id.contactsFragment -> R.color.colorTab3
                    else -> R.color.colorTab4
                }))
            }
            bottomNavView.setupWithNavController(navController)
            senseBtn.setOnClickListener { startSensorServiceWithPermissionCheck() }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        if (ACTION_SHORTCUT_LAUNCH == intent.action && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
            handleShortcutLaunch()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONTACTS_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val results = MultiContactPicker.obtainResult(data)
                EventBus.getDefault().post(ContactsEvent(results.map { contactResult -> contactResult.toContact() }))
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    /**
     * Start [SmsService] to send SMS with location to trusted contacts
     */
    @NeedsPermission(Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION)
    fun startSensorService() {
        lifecycleScope.launch {
            DbRepository.fetchAllContacts().collect { phoneNumbers ->
                val intent = Intent(this@HomeActivity, SmsService::class.java)
                        .setAction(ACTION_START_SERVICE)
                        .putExtra(EXTRA_CLASS, this@HomeActivity::class.java.name)
                        .putExtra(EXTRA_ICON_RES, R.mipmap.ic_launcher)
                        .putExtra(EXTRA_PHONE_NUMBERS, arrayOf(*phoneNumbers.toTypedArray()))
                startFgOrBgService(intent)
            }
        }
    }

    @OnShowRationale(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationale(request: PermissionRequest) {
        buildInfoDialog(R.string.loc_permission, request).show()
    }

    @OnNeverAskAgain(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    fun showSettingsDialog() {
        buildSettingsDialog(R.string.loc_permission).show()
    }

    @OnPermissionDenied(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    fun showDenied() {
        toast("Permissions denied")
    }

    /**
     * Handle shortcut launched intent
     */
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun handleShortcutLaunch() {
        val message = intent.getStringExtra(EXTRA_SHORTCUT_MESSAGE) ?: getString(R.string.shake_msg)
        lifecycleScope.launch {
            DbRepository.fetchAllContacts()
                    .map { contacts -> contacts.toPhoneNumbers() }
                    .flowOn(Dispatchers.Default)
                    .collect { contacts ->
                        val intent = buildServiceSmsIntent(message, contacts,
                                this@HomeActivity::class.java.name, R.mipmap.ic_launcher)
                        startFgOrBgService(intent)
                    }
        }
        val id = intent.getStringExtra(ShortcutManagerCompat.EXTRA_SHORTCUT_ID)
        shortcutManager?.reportShortcutUsed(id)
    }
}