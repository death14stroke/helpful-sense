package com.andruid.magic.helpfulsense.ui.adapter

import com.andruid.magic.helpfulsense.database.entity.Contact
import com.andruid.magic.helpfulsense.model.ContactHolder
import com.andruid.magic.helpfulsense.model.toContactHolder
import eu.davidea.flexibleadapter.FlexibleAdapter

class ContactAdapter(private val mListener: ActionAdapter.SwipeListener) : FlexibleAdapter<ContactHolder>(mutableListOf<ContactHolder>()) {

    override fun onItemSwiped(position: Int, direction: Int) {
        super.onItemSwiped(position, direction)
        mListener.onSwipe(position, direction)
    }

    fun setContacts(contacts: List<Contact>) {
        updateDataSet(contacts.map { contact -> contact.toContactHolder() }, true)
    }
}