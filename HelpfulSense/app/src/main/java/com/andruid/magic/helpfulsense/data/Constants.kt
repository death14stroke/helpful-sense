package com.andruid.magic.helpfulsense.data

import androidx.core.content.pm.ShortcutManagerCompat

// activity request code for selecting contacts
const val CONTACTS_PICKER_REQUEST = 0

/** EventBus action for adding new alert
 * @see ACTION_EDIT
 * @see ACTION_DIALOG_CANCEL
 * @see ACTION_SMS */
const val ACTION_ADD = "action_add"
/** EventBus action for editing an alert
 * @see ACTION_ADD
 * @see ACTION_DIALOG_CANCEL
 * @see ACTION_SMS */
const val ACTION_EDIT = "action_edit"
/** EventBus action for dialog cancelled
 * @see ACTION_ADD
 * @see ACTION_EDIT
 * @see ACTION_SMS */
const val ACTION_DIALOG_CANCEL = "action_dialog_cancel"
/** EventBus action for sending alert SMS
 * @see ACTION_ADD
 * @see ACTION_EDIT
 * @see ACTION_DIALOG_CANCEL */
const val ACTION_SMS = "action_sms"

/** Intent action when a shortcut is launched
 * @see ShortcutManagerCompat.EXTRA_SHORTCUT_ID
 * @see EXTRA_SHORTCUT_MESSAGE */
const val ACTION_SHORTCUT_LAUNCH = "com.andruid.magic.helpfulsense.SHORTCUT_LAUNCH"

/** Message which will be sent via SMS
 * @see ACTION_SHORTCUT_LAUNCH */
const val EXTRA_SHORTCUT_MESSAGE = "shortcut_message"

// shortcut ID for alert shortcut
const val SHORTCUT_ALERT = "alert"
// shortcut ID prefix for category actions shortcuts
const val SHORTCUT_ACTION = "action"