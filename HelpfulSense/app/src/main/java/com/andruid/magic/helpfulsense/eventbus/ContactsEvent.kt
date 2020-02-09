package com.andruid.magic.helpfulsense.eventbus

import com.andruid.magic.helpfulsense.database.entity.Contact

data class ContactsEvent(
        val results: List<Contact>
)