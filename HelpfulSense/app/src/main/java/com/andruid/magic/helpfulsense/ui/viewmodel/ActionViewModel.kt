package com.andruid.magic.helpfulsense.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.andruid.magic.helpfulsense.database.DbRepository

class ActionViewModel : ViewModel() {
    val actionLiveData = DbRepository.getInstance().fetchLiveActions()
}