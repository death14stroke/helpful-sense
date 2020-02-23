package com.andruid.magic.helpfulsense.ui.fragment

import android.Manifest
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.database.DbRepository
import com.andruid.magic.helpfulsense.databinding.FragmentMessageBinding
import com.andruid.magic.helpfulsense.ui.activity.HomeActivity
import com.andruid.magic.helpfulsense.ui.util.buildInfoDialog
import com.andruid.magic.helpfulsense.ui.util.buildSettingsDialog
import com.andruid.magic.helpfulsense.database.entity.toPhoneNumbers
import com.andruid.magic.library.startFgOrBgService
import com.andruid.magic.locationsms.service.SmsService
import com.andruid.magic.locationsms.util.buildServiceSmsIntent
import kotlinx.coroutines.launch
import permissions.dispatcher.*
import splitties.toast.toast

/**
 * Fragment to send custom alert message with location
 */
@RuntimePermissions
class MessageFragment : Fragment() {
    private lateinit var binding: FragmentMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false)
        return with(binding) {
            messageET.movementMethod = ScrollingMovementMethod()
            sendBtn.setOnClickListener { sendSMSWithPermissionCheck() }
            root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
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
            val phoneNumbers = DbRepository.getInstance().fetchContacts().toPhoneNumbers()
            val intent = requireContext().buildServiceSmsIntent(message, phoneNumbers,
                    HomeActivity::class.java.name, R.mipmap.ic_launcher)
            startFgOrBgService(intent)
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