package com.andruid.magic.helpfulsense.ui.adapter

import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.model.ActionHolder
import com.andruid.magic.helpfulsense.model.toActionHolder
import eu.davidea.flexibleadapter.FlexibleAdapter

class ActionAdapter(private val mListener: SwipeListener) : FlexibleAdapter<ActionHolder>(mutableListOf<ActionHolder>()) {

    override fun onItemSwiped(position: Int, direction: Int) {
        super.onItemSwiped(position, direction)
        mListener.onSwipe(position, direction)
    }

    fun setActions(actions: List<Action>) {
        updateDataSet(actions.map { action -> action.toActionHolder() }, true)
    }

    interface SwipeListener {
        fun onSwipe(position: Int, direction: Int)
    }
}