package com.andruid.magic.helpfulsense.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wafflecopter.multicontactpicker.ContactResult
import com.wafflecopter.multicontactpicker.RxContacts.PhoneNumber

@Entity(tableName = "contacts")
data class Contact(
        @PrimaryKey
        val contactID: String,
        val name: String,
        val phoneNumbers: List<PhoneNumber>
)

fun ContactResult.toContact(): Contact {
    return Contact(contactID = contactID,
            name = displayName,
            phoneNumbers = phoneNumbers.distinctBy { phoneNumber -> phoneNumber.number })
}