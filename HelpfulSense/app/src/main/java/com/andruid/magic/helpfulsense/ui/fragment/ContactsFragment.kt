package com.andruid.magic.helpfulsense.ui.fragment

import android.Manifest
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.data.CONTACTS_PICKER_REQUEST
import com.andruid.magic.helpfulsense.database.DbRepository
import com.andruid.magic.helpfulsense.databinding.FragmentContactsBinding
import com.andruid.magic.helpfulsense.eventbus.ContactsEvent
import com.andruid.magic.helpfulsense.ui.adapter.ActionAdapter
import com.andruid.magic.helpfulsense.ui.adapter.ContactAdapter
import com.andruid.magic.helpfulsense.ui.util.buildInfoDialog
import com.andruid.magic.helpfulsense.ui.util.buildSettingsDialog
import com.andruid.magic.helpfulsense.ui.viewmodel.ContactViewModel
import com.wafflecopter.multicontactpicker.LimitColumn
import com.wafflecopter.multicontactpicker.MultiContactPicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*
import splitties.toast.toast
import timber.log.Timber

@RuntimePermissions
class ContactsFragment : Fragment(), ActionAdapter.SwipeListener {
    companion object {
        private const val MAX_CONTACTS = 5
    }

    private lateinit var binding: FragmentContactsBinding

    private val contactViewModel by viewModels<ContactViewModel>()
    private val contactsAdapter = ContactAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container, false)
        binding.apply {
            recyclerView.apply {
                adapter = contactsAdapter
                itemAnimator = DefaultItemAnimator()
                addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
                setEmptyViewClickListener { openContactsPickerWithPermissionCheck() }
            }
            contactsAdapter.isSwipeEnabled = true
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        contactViewModel.contactLiveData.observe(this) { contacts ->
            contactsAdapter.setContacts(contacts)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_contacts, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit_contacts -> openContactsPickerWithPermissionCheck()
        }
        val navController = findNavController()
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onSwipe(position: Int, direction: Int) {
        Timber.d("onSwipe: $position")
        contactsAdapter.getItem(position)?.contact?.let { contact ->
            lifecycleScope.launch(Dispatchers.IO) { DbRepository.getInstance().delete(contact) }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onContactsEvent(contactsEvent: ContactsEvent) {
        val contacts = contactsEvent.results
        lifecycleScope.launch(Dispatchers.IO) { DbRepository.getInstance().insertAll(contacts) }
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    fun openContactsPicker() {
        lifecycleScope.launch {
            val contacts = DbRepository.getInstance().fetchContacts()
            MultiContactPicker.Builder(requireActivity())
                    .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                            android.R.anim.fade_in, android.R.anim.fade_out)
                    .limitToColumn(LimitColumn.PHONE)
                    .showTrack(true)
                    .setSelectedContacts(*contacts.map { contact -> contact.contactID }.toTypedArray())
                    .setSelectionLimit(MAX_CONTACTS)
                    .showPickerForResult(CONTACTS_PICKER_REQUEST)
        }
    }

    @OnShowRationale(Manifest.permission.READ_CONTACTS)
    fun showRationale(request: PermissionRequest) {
        buildInfoDialog(R.string.contact_permission, request).show()
    }

    @OnNeverAskAgain(Manifest.permission.READ_CONTACTS)
    fun showSettingsDialog() {
        buildSettingsDialog(R.string.contact_permission).show()
    }

    @OnPermissionDenied(Manifest.permission.READ_CONTACTS)
    fun showDenied() {
        toast("Permission denied")
    }
}