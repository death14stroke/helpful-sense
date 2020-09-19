package com.andruid.magic.helpfulsense.ui.fragment

import android.Manifest
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.andruid.magic.eezetensions.startFgOrBgService
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.database.DbRepository
import com.andruid.magic.helpfulsense.database.entity.toPhoneNumbers
import com.andruid.magic.helpfulsense.databinding.FragmentMessageBinding
import com.andruid.magic.helpfulsense.ui.activity.HomeActivity
import com.andruid.magic.helpfulsense.ui.util.buildInfoDialog
import com.andruid.magic.helpfulsense.ui.util.buildSettingsDialog
import com.andruid.magic.helpfulsense.ui.viewbinding.viewBinding
import com.andruid.magic.locationsms.service.SmsService
import com.andruid.magic.locationsms.util.buildServiceSmsIntent
import com.andruid.magic.locationsms.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import permissions.dispatcher.*

/**
 * Fragment to send custom alert message with location
 */
@RuntimePermissions
class MessageFragment : Fragment(R.layout.fragment_message) {
    private val binding by viewBinding(FragmentMessageBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            messageET.movementMethod = ScrollingMovementMethod()
            sendBtn.setOnClickListener { sendSMSWithPermissionCheck() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_help, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController()
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    /**
     * Launch [SmsService] to send custom SMS with location to trusted contacts
     */
    @NeedsPermission(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE)
    fun sendSMS() {
        lifecycleScope.launch {
            val message = binding.messageET.text.toString().trim { it <= ' ' }
            DbRepository.fetchAllContacts()
                    .map { contacts -> contacts.toPhoneNumbers() }
                    .flowOn(Dispatchers.Default)
                    .collect { phoneNumbers ->
                        val intent = requireContext().buildServiceSmsIntent(message, phoneNumbers,
                                HomeActivity::class.java.name, R.mipmap.ic_launcher)
                        startFgOrBgService(intent)
                    }
        }
    }

    @OnShowRationale(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE)
    fun showRationale(request: PermissionRequest) {
        buildInfoDialog(R.string.sms_permission, request).show()
    }

    @OnPermissionDenied(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE)
    fun showDenied() {
        toast("Denied permission")
    }

    @OnNeverAskAgain(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE)
    fun showSettingsDialog() {
        buildSettingsDialog(R.string.sms_permission).show()
    }
}