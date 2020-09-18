package com.andruid.magic.helpfulsense.ui.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.andruid.magic.eezetensions.buildSettingsIntent
import com.andruid.magic.helpfulsense.R
import permissions.dispatcher.PermissionRequest

/**
 * Dialog to guide user to app settings to grant all permissions
 * @param msgRes string resource as dialog text
 * @return alert dialog
 * @receiver context of the calling component
 */
fun Context.buildSettingsDialog(@StringRes msgRes: Int): AlertDialog {
    return materialAlertDialog {
        setMessage(msgRes)
        positiveButton(R.string.settings) { startActivity(buildSettingsIntent()) }
        cancelButton()
    }
}

/**
 * Dialog to guide user to app settings to grant all permissions
 * @param msgRes string resource as dialog text
 * @return alert dialog
 * @receiver active fragment
 */
fun Fragment.buildSettingsDialog(@StringRes msgRes: Int) = requireContext().buildSettingsDialog(msgRes)

/**
 * Dialog to provide user explanation for permission asked
 * @param msgRes string resource as dialog text
 * @return alert dialog
 * @receiver context of the calling component
 */
fun Context.buildInfoDialog(@StringRes msgRes: Int, request: PermissionRequest): AlertDialog {
    return materialAlertDialog {
        setMessage(msgRes)
        okButton { request.proceed() }
        cancelButton { request.cancel() }
    }
}

/**
 * Dialog to provide user explanation for permission asked
 * @param msgRes string resource as dialog text
 * @return alert dialog
 * @receiver active fragment
 */
fun Fragment.buildInfoDialog(@StringRes msgRes: Int, request: PermissionRequest) =
        requireContext().buildInfoDialog(msgRes, request)