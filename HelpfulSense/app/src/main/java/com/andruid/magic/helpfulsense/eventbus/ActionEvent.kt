package com.andruid.magic.helpfulsense.eventbus

import com.andruid.magic.helpfulsense.database.entity.Action

/**
 * EventBus object for [Action] related events
 * @property action that is added/modified/removed
 * @property command event
 */
data class ActionEvent(
        val action: Action?,
        val command: String
)