package com.andruid.magic.helpfulsense.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.andruid.magic.helpfulsense.database.DbRepository

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    val contactLiveData = DbRepository.getInstance().fetchLiveContacts()
}