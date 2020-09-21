package com.andruid.magic.helpfulsense.ui.dragdrop

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.eezetensions.color
import com.andruid.magic.eezetensions.drawable
import com.andruid.magic.helpfulsense.R

class DragCallback(
        context: Context,
        private val adapter: IDragDropContract,
        private val dragOnLongPress: Boolean = true
) : ItemTouchHelper.Callback() {
    private val deleteIcon = context.drawable(android.R.drawable.ic_menu_delete)
    private val background = GradientDrawable().apply {
        setColor(Color.RED)
        cornerRadius = context.resources.getDimensionPixelSize(R.dimen.card_corner).toFloat()
    }
    private val editIcon = context.drawable(android.R.drawable.ic_menu_edit)
    private val colorEdit = context.color(R.color.colorEdit)

    private var fromPosition = -1
    private var toPosition = -1

    override fun isLongPressDragEnabled() = dragOnLongPress

    override fun isItemViewSwipeEnabled() = true

    override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
    ): Boolean {
        if (fromPosition == -1)
            fromPosition = viewHolder.adapterPosition
        toPosition = target.adapterPosition

        adapter.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.onSwiped(position, direction)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE)
            adapter.onRowSelected(viewHolder)
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20

        val icon = if (dX > 0) editIcon else deleteIcon

        val iconMargin: Int = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop: Int = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        when {
            dX > 0 -> { // Swiping to the right
                background.setColor(colorEdit)

                val iconLeft: Int = itemView.left + iconMargin
                val iconRight: Int = itemView.left + iconMargin + icon.intrinsicWidth
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(itemView.left, itemView.top,
                        itemView.left + dX.toInt() + backgroundCornerOffset,
                        itemView.bottom)
            }
            dX < 0 -> { // Swiping to the left
                background.setColor(Color.RED)

                val iconLeft: Int = itemView.right - iconMargin - icon.intrinsicWidth
                val iconRight: Int = itemView.right - iconMargin
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(itemView.right + dX.toInt() - backgroundCornerOffset,
                        itemView.top, itemView.right, itemView.bottom)
            }
            else -> { // view is unSwiped
                background.setBounds(0, 0, 0, 0)
            }
        }

        background.draw(c)
        icon.draw(c)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        if (fromPosition != -1 && toPosition != -1 && fromPosition != toPosition) {
            adapter.onDragComplete(fromPosition, toPosition)
            fromPosition = -1
            toPosition = -1
        }

        adapter.onRowClear(viewHolder)
    }

    interface IDragDropContract {
        fun onRowMoved(fromPosition: Int, toPosition: Int)
        fun onRowSelected(viewHolder: RecyclerView.ViewHolder?)
        fun onRowClear(viewHolder: RecyclerView.ViewHolder?)
        fun onDragComplete(fromPosition: Int, toPosition: Int)
        fun onSwiped(position: Int, direction: Int)
    }

    interface StartDragListener {
        fun requestDrag(viewHolder: RecyclerView.ViewHolder)
    }
}