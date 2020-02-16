package com.andruid.magic.helpfulsense.ui.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.util.buildSettingsIntent
import permissions.dispatcher.PermissionRequest
import splitties.alertdialog.appcompat.*

fun Context.buildSettingsDialog(msgRes: Int): AlertDialog {
    return alertDialog {
        messageResource = msgRes
        positiveButton(R.string.settings) {
            startActivity(buildSettingsIntent(this@buildSettingsDialog))
        }
        cancelButton()
    }
}

fun Fragment.buildSettingsDialog(msgRes: Int) = requireContext().buildSettingsDialog(msgRes)

fun Context.buildInfoDialog(msgRes: Int, request: PermissionRequest): AlertDialog {
    return alertDialog {
        messageResource = msgRes
        okButton { request.proceed() }
        cancelButton { request.cancel() }
    }
}

fun Fragment.buildInfoDialog(msgRes: Int, request: PermissionRequest) =
        requireContext().buildInfoDialog(msgRes, request)