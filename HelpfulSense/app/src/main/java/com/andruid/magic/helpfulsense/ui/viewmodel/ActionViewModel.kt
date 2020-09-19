package com.andruid.magic.helpfulsense.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.andruid.magic.helpfulsense.database.DbRepository
import com.andruid.magic.helpfulsense.database.entity.Action

/**
 * ViewModel for getting all [Action] as [LiveData]
 */
class ActionViewModel : ViewModel() {
    val actionLiveData = liveData {
        emitSource(DbRepository.fetchAllActions().asLiveData())
    }
}