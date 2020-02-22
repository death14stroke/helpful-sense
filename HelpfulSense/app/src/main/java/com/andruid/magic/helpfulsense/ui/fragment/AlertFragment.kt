package com.andruid.magic.helpfulsense.ui.fragment

import android.Manifest
import android.content.Intent
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
import androidx.recyclerview.widget.ItemTouchHelper
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.data.*
import com.andruid.magic.helpfulsense.database.DbRepository
import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.databinding.FragmentAlertBinding
import com.andruid.magic.helpfulsense.eventbus.ActionEvent
import com.andruid.magic.helpfulsense.ui.activity.HomeActivity
import com.andruid.magic.helpfulsense.ui.activity.IntroActivity
import com.andruid.magic.helpfulsense.ui.adapter.ActionAdapter
import com.andruid.magic.helpfulsense.ui.util.buildInfoDialog
import com.andruid.magic.helpfulsense.ui.util.buildSettingsDialog
import com.andruid.magic.helpfulsense.ui.viewmodel.ActionViewModel
import com.andruid.magic.helpfulsense.util.toPhoneNumbers
import com.andruid.magic.library.startFgOrBgService
import com.andruid.magic.locationsms.util.buildServiceSmsIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*
import splitties.toast.toast
import timber.log.Timber

@RuntimePermissions
class AlertFragment : Fragment(), ActionAdapter.SwipeListener {
    private lateinit var binding: FragmentAlertBinding

    private val actionAdapter = ActionAdapter(this)
    private val actionViewModel by viewModels<ActionViewModel>()
    private var swipedPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alert, container, false)
        binding.apply {
            recyclerView.apply {
                adapter = actionAdapter
                itemAnimator = DefaultItemAnimator()
                setEmptyViewClickListener {
                    val navController = findNavController()
                    navController.navigate(AlertFragmentDirections.actionAlertFragmentToMenuAddAction(ACTION_ADD, null))
                }
            }
            actionAdapter.apply {
                isLongPressDragEnabled = true
                isSwipeEnabled = true
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        actionViewModel.actionLiveData.observe(viewLifecycleOwner) { actions ->
            actionAdapter.setActions(actions)
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
            R.id.menu_add_action -> AlertFragmentDirections.actionAlertFragmentToMenuAddAction(ACTION_ADD, null)
            R.id.menu_help -> startActivity(Intent(context, IntroActivity::class.java))
        }
        return item.onNavDestinationSelected(findNavController()) || super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onSwipe(position: Int, direction: Int) {
        Timber.d("onSwipe: $position")
        swipedPos = position
        actionAdapter.getItem(position)?.action?.let { action ->
            when (direction) {
                ItemTouchHelper.LEFT, ItemTouchHelper.START ->
                    lifecycleScope.launch(Dispatchers.IO) { DbRepository.getInstance().delete(action) }
                else -> {
                    val navController = findNavController()
                    navController.navigate(AlertFragmentDirections.actionAlertFragmentToMenuAddAction(ACTION_SWIPE, action))
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onActionEvent(actionEvent: ActionEvent) {
        actionEvent.action?.let {
            when (actionEvent.command) {
                ACTION_ADD, ACTION_EDIT -> lifecycleScope.launch(Dispatchers.IO) { DbRepository.getInstance().insert(it) }
                ACTION_DIALOG_CANCEL -> actionAdapter.notifyDataSetChanged()
                ACTION_SMS -> sendSMSWithPermissionCheck(it)
            }
            null
        }
    }

    @NeedsPermission(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION)
    fun sendSMS(action: Action) {
        lifecycleScope.launch {
            val phoneNumbers = DbRepository.getInstance().fetchContacts().toPhoneNumbers()
            val intent = requireContext().buildServiceSmsIntent(action.message, phoneNumbers,
                    HomeActivity::class.java.name, R.mipmap.ic_launcher)
            startFgOrBgService(intent)
        }
    }

    @OnShowRationale(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationale(request: PermissionRequest) {
        buildInfoDialog(R.string.sms_permission, request).show()
    }

    @OnPermissionDenied(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION)
    fun showDenied() {
        toast("Denied permission")
    }

    @OnNeverAskAgain(Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION)
    fun showSettingsDialog() {
        buildSettingsDialog(R.string.sms_permission).show()
    }
}