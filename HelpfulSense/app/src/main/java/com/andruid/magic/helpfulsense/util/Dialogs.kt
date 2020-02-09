package com.andruid.magic.helpfulsense.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.database.entity.Action
import com.andruid.magic.helpfulsense.ui.fragment.ActionDialogFragment
import permissions.dispatcher.PermissionRequest
import splitties.alertdialog.appcompat.*

fun FragmentManager.openAddActionDialog(tag: String, command: String, action: Action?) {
    val dialogFragment: DialogFragment = ActionDialogFragment.newInstance(command, action)
    dialogFragment.show(this, tag)
}

fun Context.buildSettingsDialog(msgRes: Int): AlertDialog {
    return alertDialog {
        messageResource = msgRes
        positiveButton(R.string.settings) {
            startActivity(buildSettingsIntent(this@buildSettingsDialog)) }
        cancelButton()
    }
}

fun Context.buildInfoDialog(msgRes: Int, request: PermissionRequest): AlertDialog {
    return alertDialog {
        messageResource = msgRes
        okButton { request.proceed() }
        cancelButton { request.cancel() }
    }
}