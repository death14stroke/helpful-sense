package com.andruid.magic.helpfulsense.util;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.andruid.magic.helpfulsense.fragment.ActionDialogFragment;
import com.andruid.magic.helpfulsense.model.Action;

import permissions.dispatcher.PermissionRequest;

public class DialogUtil {
    public static void openAddActionDialog(FragmentManager fm, String tag, String command,
                                           Action action){
        DialogFragment dialogFragment = ActionDialogFragment.newInstance(command, action);
        dialogFragment.show(fm, tag);
    }

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