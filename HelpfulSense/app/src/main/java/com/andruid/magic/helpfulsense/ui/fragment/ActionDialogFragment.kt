package com.andruid.magic.helpfulsense.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.andruid.magic.helpfulsense.data.ACTION_DIALOG_CANCEL
import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.databinding.DialogActionBinding
import com.andruid.magic.helpfulsense.eventbus.ActionEvent
import com.andruid.magic.helpfulsense.model.Category
import com.andruid.magic.helpfulsense.ui.adapter.MySpinnerAdapter
import com.andruid.magic.helpfulsense.ui.util.cancelButton
import com.andruid.magic.helpfulsense.ui.util.materialAlertDialog
import com.andruid.magic.helpfulsense.ui.util.okButton
import com.andruid.magic.helpfulsense.util.getCategoryFromRes
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

/**
 * Dialog to add/edit [Action]
 */
class ActionDialogFragment : DialogFragment() {
    private lateinit var binding: DialogActionBinding

    // add/edit mode
    private lateinit var command: String

    private var action: Action? = null

    private val mySpinnerAdapter by lazy {
        MySpinnerAdapter(requireContext().getCategoryFromRes())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogActionBinding.inflate(LayoutInflater.from(context), null, false)
        binding.spinner.adapter = mySpinnerAdapter
        arguments?.let { args ->
            val safeArgs = ActionDialogFragmentArgs.fromBundle(args)
            action = safeArgs.action
            command = safeArgs.command
            Timber.tag("actionLog").d("onCreateDialog: action = ${action?.message ?: "null"}, cmd = $command")
            val categories = mySpinnerAdapter.categories
            action?.let {
                binding.messageET.setText(it.message)
                val pos = IntRange(0, categories.size - 1)
                        .find { i: Int -> it.category.name == categories[i].name }
                binding.spinner.setSelection(pos ?: 0)
            }
        }
        val header = "${action?.let { "Edit" } ?: "Add"} action"
        return requireActivity().materialAlertDialog {
            setTitle(header)
            setView(binding.root)
            okButton {
                val message = binding.messageET.text.toString().trim()
                val category = binding.spinner.selectedItem as Category
                val action = action?.copy(message = message, category = category)
                        ?: Action(message = message, category = category)
                EventBus.getDefault().post(ActionEvent(action, command))
            }
            cancelButton {
                EventBus.getDefault().post(ActionEvent(null, ACTION_DIALOG_CANCEL))
            }
        }
    }
}