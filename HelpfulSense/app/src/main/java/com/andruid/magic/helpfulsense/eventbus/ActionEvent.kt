package com.andruid.magic.helpfulsense.eventbus

import com.andruid.magic.helpfulsense.database.entity.Action

data class ActionEvent(
        val action: Action?,
        val command: String
)