package com.andruid.magic.helpfulsense.model

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.ui.adapter.ActionAdapter
import com.andruid.magic.helpfulsense.ui.viewholder.ActionViewHolder
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * Holder class for creating swipeable and draggable views for [ActionAdapter]
 * @property action data for the view
 *//*

data class ActionHolder(
        val action: Action
) : AbstractFlexibleItem<ActionViewHolder>() {
    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
                                holder: ActionViewHolder?, position: Int, payloads: MutableList<Any>?) {
        holder?.bind((adapter?.getItem(position) as ActionHolder).action, dra)
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?) =
            ActionViewHolder(view, adapter)

    override fun getLayoutRes() = R.layout.layout_action
}

*/
/**
 * Extension function to convert [Action] object to [ActionHolder] for recycler view
 * @receiver action object
 * @return actionHolder object
 *//*

fun Action.toActionHolder() = ActionHolder(this)*/
