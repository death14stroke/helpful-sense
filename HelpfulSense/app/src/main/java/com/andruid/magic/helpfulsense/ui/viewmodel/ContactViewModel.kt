package com.andruid.magic.helpfulsense.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.andruid.magic.helpfulsense.database.DbRepository
import com.andruid.magic.helpfulsense.database.entity.Contact

/**
 * ViewModel for getting all [Contact] as [LiveData]
 */
class ContactViewModel : ViewModel() {
    val contactLiveData = DbRepository.fetchLiveContacts()
}