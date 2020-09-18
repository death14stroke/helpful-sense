package com.andruid.magic.helpfulsense.ui.util

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

inline fun Context.materialAlertDialog(dialogConfig: MaterialAlertDialogBuilder.() -> Unit): AlertDialog {
    return MaterialAlertDialogBuilder(this)
            .apply(dialogConfig)
            .create()
}

inline fun MaterialAlertDialogBuilder.cancelButton(crossinline handler: (dialog: DialogInterface) -> Unit) {
    setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int -> handler(dialog) }
}

fun MaterialAlertDialogBuilder.cancelButton() {
    setNegativeButton(android.R.string.cancel, null)
}

inline fun MaterialAlertDialogBuilder.positiveButton(@StringRes textResId: Int, crossinline handler: (dialog: DialogInterface) -> Unit) {
    setPositiveButton(textResId) { dialog: DialogInterface, _: Int -> handler(dialog) }
}

inline fun MaterialAlertDialogBuilder.okButton(crossinline handler: (dialog: DialogInterface) -> Unit) {
    setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int -> handler(dialog) }
}