package com.andruid.magic.helpfulsense.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.data.CONTACTS_PICKER_REQUEST
import com.andruid.magic.helpfulsense.database.entity.toContact
import com.andruid.magic.helpfulsense.databinding.ActivityHomeBinding
import com.andruid.magic.helpfulsense.eventbus.ContactsEvent
import com.andruid.magic.helpfulsense.service.SensorService
import com.andruid.magic.helpfulsense.ui.util.buildInfoDialog
import com.andruid.magic.helpfulsense.ui.util.buildSettingsDialog
import com.andruid.magic.helpfulsense.util.color
import com.andruid.magic.helpfulsense.util.startFgOrBgService
import com.wafflecopter.multicontactpicker.MultiContactPicker
import org.greenrobot.eventbus.EventBus
import permissions.dispatcher.*
import splitties.toast.toast
import timber.log.Timber

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

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONTACTS_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val results = MultiContactPicker.obtainResult(data)
                Timber.d("in activity: %d selected", results.size)
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
        val intent = Intent(this, SensorService::class.java)
        startFgOrBgService(intent)
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
}