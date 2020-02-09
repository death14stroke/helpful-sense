package com.andruid.magic.helpfulsense.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.andruid.magic.helpfulsense.database.DbRepository

class ActionViewModel(application: Application) : AndroidViewModel(application) {
    val actionLiveData = DbRepository.getInstance().fetchActions()
}