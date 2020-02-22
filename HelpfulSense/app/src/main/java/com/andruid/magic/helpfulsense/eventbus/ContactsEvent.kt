package com.andruid.magic.helpfulsense.eventbus

import com.andruid.magic.helpfulsense.database.entity.Contact

/**
 * EventBus object for [Contact] related events
 * @property results selected contacts from contacts picker
 */
data class ContactsEvent(val results: List<Contact>)