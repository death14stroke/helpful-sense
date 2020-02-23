package com.andruid.magic.helpfulsense.ui.adapter

import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.model.ActionHolder
import com.andruid.magic.helpfulsense.model.toActionHolder
import eu.davidea.flexibleadapter.FlexibleAdapter

/**
 * RecyclerView Adapter for showing list of all [Action]
 * @property mListener callback for swiping left/right
 */
class ActionAdapter(private val mListener: SwipeListener) : FlexibleAdapter<ActionHolder>(mutableListOf<ActionHolder>()) {

    override fun onItemSwiped(position: Int, direction: Int) {
        super.onItemSwiped(position, direction)
        mListener.onSwipe(position, direction)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        mListener.onMove(fromPosition, toPosition)
        return super.onItemMove(fromPosition, toPosition)
    }

    /**
     * Set dataSet for the adapter
     * @param actions dataSet of all actions
     */
    fun setActions(actions: List<Action>) {
        updateDataSet(actions.map { action -> action.toActionHolder() }, true)
    }
}