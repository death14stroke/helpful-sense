package com.andruid.magic.helpfulsense.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.ui.dragdrop.DragCallback
import com.andruid.magic.helpfulsense.ui.viewholder.ActionViewHolder
import java.util.*

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Action>() {
    override fun areItemsTheSame(oldItem: Action, newItem: Action) =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Action, newItem: Action) =
            oldItem == newItem
}

class ActionAdapter(
        private val dragListener: DragCallback.StartDragListener,
        private val onDragComplete: (fromPosition: Int, toPosition: Int) -> Unit
) : ListAdapter<Action, ActionViewHolder>(DIFF_CALLBACK), DragCallback.IDragDropContract {
    private var showHandle = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ActionViewHolder.from(parent)

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        getItem(position)?.let { action ->
            holder.bind(action, showHandle, dragListener)
        }
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        val data = currentList.toMutableList()
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition)
                Collections.swap(data, i, i + 1)
        } else {
            for (i in fromPosition downTo toPosition + 1)
                Collections.swap(data, i, i - 1)
        }

        submitList(data)
    }

    override fun onRowSelected(viewHolder: RecyclerView.ViewHolder?) {}

    override fun onRowClear(viewHolder: RecyclerView.ViewHolder?) {}

    override fun onDragComplete(fromPosition: Int, toPosition: Int) {
        onDragComplete.invoke(fromPosition, toPosition)
    }

    fun showDragHandles() {
        showHandle = true
        notifyItemRangeChanged(0, currentList.size)
    }

    fun hideDragHandles() {
        showHandle = false
        notifyItemRangeChanged(0, currentList.size)
    }
}