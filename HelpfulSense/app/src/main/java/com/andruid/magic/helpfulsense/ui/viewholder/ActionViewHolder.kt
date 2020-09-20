package com.andruid.magic.helpfulsense.ui.viewholder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.eezetensions.drawable
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.data.ACTION_SMS
import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.databinding.LayoutActionBinding
import com.andruid.magic.helpfulsense.eventbus.ActionEvent
import com.andruid.magic.helpfulsense.ui.adapter.ActionAdapter
import com.andruid.magic.helpfulsense.ui.dragdrop.DragCallback
import org.greenrobot.eventbus.EventBus

/**
 * ViewHolder for [ActionAdapter]
 */
class ActionViewHolder(
        private val binding: LayoutActionBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): ActionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<LayoutActionBinding>(inflater, R.layout.layout_action, parent, false)
            return ActionViewHolder(binding)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun bind(action: Action, showHandle: Boolean, dragListener: DragCallback.StartDragListener) {
        binding.action = action

        binding.apply {
            actionTV.setCompoundDrawablesWithIntrinsicBounds(frontView.context.drawable(action.category.icon),
                    null, null, null)
            frontView.setBackgroundResource(action.category.iconColor)
            sendBtn.setOnClickListener {
                EventBus.getDefault().post(ActionEvent(action, ACTION_SMS))
            }

            dragHandleView.visibility = if (showHandle) View.VISIBLE else View.GONE

            dragHandleView.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN)
                    dragListener.requestDrag(this@ActionViewHolder)
                return@setOnTouchListener false
            }


            executePendingBindings()
        }
    }
}