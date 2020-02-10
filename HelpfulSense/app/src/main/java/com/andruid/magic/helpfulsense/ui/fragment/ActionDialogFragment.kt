package com.andruid.magic.helpfulsense.ui.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.data.*
import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.databinding.DialogActionBinding
import com.andruid.magic.helpfulsense.eventbus.ActionEvent
import com.andruid.magic.helpfulsense.model.Category
import com.andruid.magic.helpfulsense.ui.adapter.MySpinnerAdapter
import com.andruid.magic.helpfulsense.util.getCategoryFromRes
import com.annimon.stream.IntStream
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

class ActionDialogFragment : DialogFragment() {
    companion object {
        fun newInstance(command: String, action: Action?): ActionDialogFragment {
            val args = bundleOf(
                    KEY_COMMAND to command,
                    KEY_ACTION to action
            )
            return ActionDialogFragment().apply {
                arguments = args
            }
        }
    }

    private lateinit var binding: DialogActionBinding
    private lateinit var command: String

    private var action: Action? = null

    private val mySpinnerAdapter by lazy {
        MySpinnerAdapter(requireContext().getCategoryFromRes())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_action,
                null, false)
        binding.spinner.adapter = mySpinnerAdapter
        arguments?.let { args ->
            action = args.getParcelable(KEY_ACTION)
            command = requireNotNull(args.getString(KEY_COMMAND), { "KEY_COMMAND null" })
            val categories = mySpinnerAdapter.categories
            action?.let {
                binding.messageET.setText(it.message)
                val pos = IntStream.range(0, categories.size)
                        .filter { i: Int -> it.category.name == categories[i].name }
                        .findFirst()
                if (pos.isPresent)
                    binding.spinner.setSelection(pos.asInt)
            }
        }
        return AlertDialog.Builder(requireActivity())
                .setTitle(tag)
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
                    val message = binding.messageET.text.toString().trim()
                    val category = binding.spinner.selectedItem as Category
                    Timber.d("selected category %s", category.name)
                    val action = action?.copy(message = message, category = category)
                            ?: Action(message = message, category = category)
                    command = if (ACTION_SWIPE == command) ACTION_EDIT else command
                    EventBus.getDefault().post(ActionEvent(action, command))
                }
                .setNegativeButton(android.R.string.cancel) { _: DialogInterface?, _: Int ->
                    EventBus.getDefault().post(ActionEvent(null, ACTION_DIALOG_CANCEL))
                }
                .create()
    }
}