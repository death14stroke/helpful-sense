package com.andruid.magic.helpfulsense.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.data.CONTACTS_PICKER_REQUEST
import com.andruid.magic.helpfulsense.database.entity.toContact
import com.andruid.magic.helpfulsense.databinding.ActivityMainBinding
import com.andruid.magic.helpfulsense.eventbus.ContactsEvent
import com.andruid.magic.helpfulsense.service.SensorService
import com.andruid.magic.helpfulsense.ui.adapter.ViewPagerAdapter
import com.andruid.magic.helpfulsense.util.buildInfoDialog
import com.andruid.magic.helpfulsense.util.buildSettingsDialog
import com.andruid.magic.helpfulsense.util.startFgOrBgService
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.wafflecopter.multicontactpicker.MultiContactPicker
import org.greenrobot.eventbus.EventBus
import permissions.dispatcher.*
import splitties.toast.toast
import timber.log.Timber

@RuntimePermissions
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.senseBtn.setOnClickListener {
            startSensorServiceWithPermissionCheck()
        }
        val firstSelected = savedInstanceState?.getInt(getString(R.string.key_tab), 0) ?: 0
        setBottomNav(firstSelected)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(getString(R.string.key_tab), binding.viewPager.currentItem)
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

    private fun setBottomNav(firstSelected: Int) {
        val pagerAdapter = ViewPagerAdapter(this)
        binding.apply {
            viewPager.apply {
                adapter = pagerAdapter
                isUserInputEnabled = false
            }
            bottomNav.apply {
                activeColor = R.color.colorPrimary
                setMode(BottomNavigationBar.MODE_SHIFTING)
                setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)

                addItem(BottomNavigationItem(R.drawable.ic_alert, getString(R.string.alert))
                        .setActiveColorResource(R.color.colorTab1))
                addItem(BottomNavigationItem(R.drawable.ic_message, getString(R.string.message))
                        .setActiveColorResource(R.color.colorTab2))
                addItem(BottomNavigationItem(R.drawable.ic_contacts, getString(R.string.contacts))
                        .setActiveColorResource(R.color.colorTab3))
                addItem(BottomNavigationItem(R.drawable.ic_settings, getString(R.string.settings))
                        .setActiveColorResource(R.color.colorTab4))

                setFirstSelectedPosition(firstSelected)
                initialise()

                isAutoHideEnabled = true
                setFab(binding.senseBtn)
                setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
                    override fun onTabSelected(position: Int) {
                        binding.viewPager.currentItem = position
                    }

                    override fun onTabUnselected(position: Int) {}
                    override fun onTabReselected(position: Int) {}
                })
            }
        }
    }
}