package com.andruid.magic.helpfulsense.ui.viewholder

import android.view.View
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.database.entity.Contact
import com.andruid.magic.helpfulsense.databinding.LayoutContactBinding
import com.andruid.magic.helpfulsense.ui.adapter.ContactAdapter
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * ViewHolder for [ContactAdapter]
 */
class ContactViewHolder(view: View, adapter: FlexibleAdapter<out IFlexible<*>>?) : FlexibleViewHolder(view, adapter) {
    private val binding = LayoutContactBinding.bind(view)

    /**
     * Bind data to the view
     * @param contact data for the view
     */
    fun bind(contact: Contact) {
        binding.apply {
            this.contact = contact
            frontView.setBackgroundResource(R.color.colorBg)
            executePendingBindings()
        }
    }

    override fun getFrontView() = binding.frontView

    override fun getRearLeftView() = binding.rearLeftLayout.rearView

    override fun getRearRightView() = binding.rearRightLayout.rearView
}