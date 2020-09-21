package com.andruid.magic.helpfulsense.ui.fragment

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.eezetensions.startFgOrBgService
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.data.ACTION_ADD
import com.andruid.magic.helpfulsense.data.ACTION_DIALOG_CANCEL
import com.andruid.magic.helpfulsense.data.ACTION_EDIT
import com.andruid.magic.helpfulsense.data.ACTION_SMS
import com.andruid.magic.helpfulsense.database.DbRepository
import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.database.entity.toPhoneNumbers
import com.andruid.magic.helpfulsense.databinding.FragmentAlertBinding
import com.andruid.magic.helpfulsense.eventbus.ActionEvent
import com.andruid.magic.helpfulsense.ui.activity.HomeActivity
import com.andruid.magic.helpfulsense.ui.activity.IntroActivity
import com.andruid.magic.helpfulsense.ui.adapter.ActionAdapter
import com.andruid.magic.helpfulsense.ui.custom.ItemClickListener
import com.andruid.magic.helpfulsense.ui.dragdrop.DragCallback
import com.andruid.magic.helpfulsense.ui.util.buildInfoDialog
import com.andruid.magic.helpfulsense.ui.util.buildSettingsDialog
import com.andruid.magic.helpfulsense.ui.viewbinding.viewBinding
import com.andruid.magic.helpfulsense.ui.viewmodel.ActionViewModel
import com.andruid.magic.locationsms.util.buildServiceSmsIntent
import com.andruid.magic.locationsms.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*
import timber.log.Timber

/**
 * Fragment to show all added actions in the database
 */
@RuntimePermissions
class AlertFragment : Fragment(R.layout.fragment_alert), DragCallback.StartDragListener {
    private val binding by viewBinding(FragmentAlertBinding::bind)
    private val touchHelper by lazy {
        val callback = DragCallback(requireContext(), actionAdapter, false)
        ItemTouchHelper(callback)
    }
    private val actionAdapter by lazy {
        ActionAdapter(this, { fromPosition, toPosition ->
            lifecycleScope.launch { updateOrder(fromPosition, toPosition) }
        }) { position, direction ->
            handleSwipe(position, direction)
        }
    }

    private val actionViewModel by viewModels<ActionViewModel>()
    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            actionMode = mode
            mode.menuInflater.inflate(R.menu.menu_action, menu)
            actionAdapter.showDragHandles()
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu) = false

        override fun onDestroyActionMode(mode: ActionMode) {
            actionAdapter.hideDragHandles()
            actionMode = null
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            hideActionMode()
            return true
        }
    }

    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        EventBus.getDefault().register(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        actionViewModel.actionLiveData.observe(viewLifecycleOwner) { actions ->
            actionAdapter.submitList(actions)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_action, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_action -> findNavController().navigate(AlertFragmentDirections.actionAlertFragmentToMenuAddAction(ACTION_ADD, null))
            R.id.menu_help -> startActivity(Intent(context, IntroActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        Timber.tag("actionLog").d("event bus unregister alert fragment")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onActionEvent(actionEvent: ActionEvent) {
        Timber.tag("actionLog").d("action event: ${actionEvent.command}")
        actionEvent.action?.let {
            when (actionEvent.command) {
                ACTION_ADD, ACTION_EDIT -> lifecycleScope.launch(Dispatchers.IO) {
                    Timber.tag("actionLog").d("action event: add/edit for ${it.message}")
                    DbRepository.insert(it, actionEvent.command == ACTION_EDIT)
                }
                ACTION_DIALOG_CANCEL -> actionAdapter.notifyDataSetChanged()
                ACTION_SMS -> sendSMSWithPermissionCheck(it)
            }
            null
        }
    }

    /**
     * Send SMS with the selected action message
     * @param action selected action
     */
    @NeedsPermission(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE)
    fun sendSMS(action: Action) {
        lifecycleScope.launch {
            DbRepository.fetchAllContacts()
                    .flowOn(Dispatchers.Default)
                    .map { contacts -> contacts.toPhoneNumbers() }
                    .collect { phoneNumbers ->
                        val intent = requireContext().buildServiceSmsIntent(action.message, phoneNumbers,
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

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            touchHelper.attachToRecyclerView(this)

            adapter = actionAdapter
            itemAnimator = DefaultItemAnimator()
            setEmptyViewClickListener {
                val navController = findNavController()
                navController.navigate(AlertFragmentDirections.actionAlertFragmentToMenuAddAction(ACTION_ADD, null))
            }
            addOnItemTouchListener(object : ItemClickListener(requireContext(), this) {
                override fun onLongClick(view: View, position: Int) {
                    super.onLongClick(view, position)
                    if (actionMode == null)
                        showActionMode()
                }
            })
        }
    }

    private fun hideActionMode() {
        actionMode?.finish()
    }

    private fun showActionMode() {
        if (actionMode == null)
            (requireActivity() as AppCompatActivity).startSupportActionMode(actionModeCallback)
    }

    private suspend fun updateOrder(fromPos: Int, toPos: Int) {
        val actions = actionAdapter.currentList.toList()
        if (fromPos < toPos) {
            for (i in fromPos..toPos) {
                val action = actions[i]
                DbRepository.insert(action.copy(order = i), true)
            }
        } else if (fromPos > toPos) {
            for (i in fromPos downTo toPos) {
                val action = actions[i]
                DbRepository.insert(action.copy(order = i), true)
            }
        }
    }

    private fun handleSwipe(position: Int, direction: Int) {
        actionAdapter.getItemAtPosition(position)?.let { action ->
            when (direction) {
                ItemTouchHelper.LEFT, ItemTouchHelper.START ->
                    lifecycleScope.launch(Dispatchers.IO) { DbRepository.delete(action) }
                else -> {
                    actionAdapter.notifyItemChanged(position)
                    val navController = findNavController()
                    navController.navigate(AlertFragmentDirections.actionAlertFragmentToMenuAddAction(ACTION_EDIT, action))
                }
            }
        }
    }

    override fun requestDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(viewHolder)
    }
}