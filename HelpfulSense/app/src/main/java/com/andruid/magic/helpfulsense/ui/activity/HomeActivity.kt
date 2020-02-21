package com.andruid.magic.helpfulsense.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.data.ACTION_SHORTCUT_LAUNCH
import com.andruid.magic.helpfulsense.data.CONTACTS_PICKER_REQUEST
import com.andruid.magic.helpfulsense.data.EXTRA_SHORTCUT_MESSAGE
import com.andruid.magic.helpfulsense.database.DbRepository
import com.andruid.magic.helpfulsense.database.entity.toContact
import com.andruid.magic.helpfulsense.databinding.ActivityHomeBinding
import com.andruid.magic.helpfulsense.eventbus.ContactsEvent
import com.andruid.magic.helpfulsense.ui.util.buildInfoDialog
import com.andruid.magic.helpfulsense.ui.util.buildSettingsDialog
import com.andruid.magic.helpfulsense.util.color
import com.andruid.magic.helpfulsense.util.startFgOrBgService
import com.andruid.magic.helpfulsense.util.toPhoneNumbers
import com.andruid.magic.locationsms.data.ACTION_START_SERVICE
import com.andruid.magic.locationsms.data.EXTRA_PHONE_NUMBERS
import com.andruid.magic.locationsms.service.SmsService
import com.andruid.magic.locationsms.util.buildServiceSmsIntent
import com.wafflecopter.multicontactpicker.MultiContactPicker
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import permissions.dispatcher.*
import splitties.systemservices.shortcutManager
import splitties.toast.toast

@RuntimePermissions
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        val navController = findNavController(R.id.nav_host_fragment)
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

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
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

    @NeedsPermission(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION)
    fun startSensorService() {
        lifecycleScope.launch {
            val phoneNumbers = DbRepository.getInstance().fetchContacts().toPhoneNumbers()
            val intent = Intent(this@HomeActivity, SmsService::class.java)
                    .setAction(ACTION_START_SERVICE)
                    .putExtra(EXTRA_PHONE_NUMBERS, arrayOf(*phoneNumbers.toTypedArray()))
            startFgOrBgService(intent)
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

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun handleShortcutLaunch() {
        val message = intent.getStringExtra(EXTRA_SHORTCUT_MESSAGE) ?: getString(R.string.shake_msg)
        lifecycleScope.launch {
            val contacts = DbRepository.getInstance().fetchContacts().toPhoneNumbers()
            val intent = buildServiceSmsIntent(this@HomeActivity, message, contacts,
                    this@HomeActivity::class.java.name, R.mipmap.ic_launcher)
            startFgOrBgService(intent)
        }
        val id = intent.getStringExtra(ShortcutManagerCompat.EXTRA_SHORTCUT_ID)
        shortcutManager?.reportShortcutUsed(id)
    }
}