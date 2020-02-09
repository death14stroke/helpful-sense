package com.andruid.magic.helpfulsense.ui.fragment

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.databinding.FragmentMessageBinding
import com.andruid.magic.helpfulsense.ui.activity.IntroActivity
import com.andruid.magic.helpfulsense.util.buildInfoDialog
import com.andruid.magic.helpfulsense.util.buildServiceSmsIntent
import com.andruid.magic.helpfulsense.util.buildSettingsDialog
import com.andruid.magic.helpfulsense.util.startFgOrBgService
import permissions.dispatcher.*
import splitties.toast.toast

@RuntimePermissions
class MessageFragment : Fragment() {
    companion object {
        fun newInstance() = MessageFragment()
    }

    private lateinit var binding: FragmentMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false)
        return with(binding) {
            sendBtn.setOnClickListener {
                sendSMSWithPermissionCheck()
            }
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
        if (item.itemId == R.id.menu_help)
            startActivity(Intent(requireContext(), IntroActivity::class.java))
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.SEND_SMS)
    fun sendSMS() {
        val message = binding.messageET.text.toString().trim { it <= ' ' }
        val intent = buildServiceSmsIntent(requireContext(), message)
        requireContext().startFgOrBgService(intent)
    }

    @OnShowRationale(Manifest.permission.SEND_SMS)
    fun showRationale(request: PermissionRequest) {
        requireContext().buildInfoDialog(R.string.sms_permission, request).show()
    }

    @OnPermissionDenied(Manifest.permission.SEND_SMS)
    fun showDenied() {
        toast("Denied permission")
    }

    @OnNeverAskAgain(Manifest.permission.SEND_SMS)
    fun showSettingsDialog() {
        requireContext().buildSettingsDialog(R.string.sms_permission).show()
    }
}