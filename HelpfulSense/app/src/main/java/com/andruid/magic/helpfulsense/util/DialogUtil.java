package com.andruid.magic.helpfulsense.util;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import permissions.dispatcher.PermissionRequest;

public class DialogUtil {
    public static AlertDialog.Builder buildSettingsDialog(Context context, String message){
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("Settings", (dialog, which) ->
                        context.startActivity(IntentUtil.buildSettingsIntent(context))
                )
                .setNegativeButton("Deny", (dialog, which) -> dialog.dismiss());
    }

    public static AlertDialog.Builder buildInfoDialog(Context context, String message,
                                                      PermissionRequest request){
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("Allow", (dialog, which) -> request.proceed())
                .setNegativeButton("Deny", (dialog, which) -> request.cancel());
    }
}