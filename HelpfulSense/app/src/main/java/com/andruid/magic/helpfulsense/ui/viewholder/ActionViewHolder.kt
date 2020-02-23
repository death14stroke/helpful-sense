package com.andruid.magic.helpfulsense.ui.viewholder

import android.view.View
import com.andruid.magic.helpfulsense.data.ACTION_SMS
import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.databinding.LayoutActionBinding
import com.andruid.magic.helpfulsense.eventbus.ActionEvent
import com.andruid.magic.helpfulsense.ui.adapter.ActionAdapter
import com.andruid.magic.library.drawable
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import org.greenrobot.eventbus.EventBus

/**
 * ViewHolder for [ActionAdapter]
 */
class ActionViewHolder(view: View, adapter: FlexibleAdapter<out IFlexible<*>>?) : FlexibleViewHolder(view, adapter) {
    private val binding = LayoutActionBinding.bind(view)

    /**
     * Bind data to the view
     * @param action data for the view
     */
    fun bind(action: Action) {
        binding.apply {
            this.action = action

            actionTV.setCompoundDrawablesWithIntrinsicBounds(frontView.context.drawable(action.category.icon),
                    null, null, null)
            frontView.setBackgroundResource(action.category.iconColor)
            sendBtn.setOnClickListener {
                EventBus.getDefault().post(ActionEvent(action, ACTION_SMS))
            }
            executePendingBindings()
        }
    }

    override fun getFrontView() = binding.frontView

    override fun getRearLeftView() = binding.rearLeftLayout.rearLeftView

    override fun getRearRightView() = binding.rearRightLayout.rearView
}