package com.andruid.magic.helpfulsense.ui.adapter

import com.andruid.magic.helpfulsense.database.entity.Contact
import com.andruid.magic.helpfulsense.model.ContactHolder
import com.andruid.magic.helpfulsense.model.toContactHolder
import eu.davidea.flexibleadapter.FlexibleAdapter

/**
 * RecyclerView Adapter for showing list of all [Contact]
 * @property mListener callback for swiping left/right
 */
class ContactAdapter(private val mListener: SwipeListener) : FlexibleAdapter<ContactHolder>(mutableListOf<ContactHolder>()) {

    override fun onItemSwiped(position: Int, direction: Int) {
        super.onItemSwiped(position, direction)
        mListener.onSwipe(position, direction)
    }

    /**
     * Set dataSet for the adapter
     * @param contacts dataSet of all contacts
     */
    fun setContacts(contacts: List<Contact>) {
        updateDataSet(contacts.map { contact -> contact.toContactHolder() }, true)
    }
}