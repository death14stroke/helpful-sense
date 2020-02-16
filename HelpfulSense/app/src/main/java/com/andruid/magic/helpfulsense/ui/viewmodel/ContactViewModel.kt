package com.andruid.magic.helpfulsense.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.andruid.magic.helpfulsense.database.DbRepository

class ContactViewModel : ViewModel() {
    val contactLiveData = DbRepository.getInstance().fetchLiveContacts()
}