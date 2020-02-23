package com.andruid.magic.helpfulsense.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wafflecopter.multicontactpicker.ContactResult
import com.wafflecopter.multicontactpicker.RxContacts.PhoneNumber

/**
 * Database entity for storing emergency contacts as defined by user
 * @property contactID unique id for the contact [PrimaryKey]
 * @property name display name of the contact
 * @property phoneNumbers list of all numbers for the contact
 */
@Entity(tableName = "contacts")
data class Contact(
        @PrimaryKey
        val contactID: String,
        val name: String,
        val phoneNumbers: List<PhoneNumber>
)

/**
 * Extension function to convert [ContactResult] object to [Contact]
 * @return contact object
 * @receiver contactResult object
 */
fun ContactResult.toContact(): Contact {
    return Contact(contactID = contactID,
            name = displayName,
            phoneNumbers = phoneNumbers.distinctBy { phoneNumber -> phoneNumber.number })
}

/**
 * Extension function to convert list of [Contact] objects to distinct phoneNumbers strings
 * @return list of string phone numbers
 * @receiver list of contacts
 */
fun List<Contact>.toPhoneNumbers(): List<String> {
    val numbers = mutableListOf<String>()
    forEach {
        val phoneNumbers = it.phoneNumbers.distinctBy { phoneNumber -> phoneNumber.number }
                .map { phoneNumber -> phoneNumber.number }
        numbers.addAll(phoneNumbers)
    }
    return numbers
}