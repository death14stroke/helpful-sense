package com.andruid.magic.helpfulsense.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.databinding.DialogActionBinding;
import com.andruid.magic.helpfulsense.eventbus.ActionEvent;
import com.andruid.magic.helpfulsense.model.Action;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

import static com.andruid.magic.helpfulsense.data.Constants.ACTION_ADD;

public class ActionDialogFragment extends DialogFragment {
    private DialogActionBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_action,
                null, false);
        return new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setTitle(R.string.add_action)
                .setView(binding.getRoot())
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String message = binding.messageET.getText().toString().trim();
                    Action action = new Action(message);
                    EventBus.getDefault().post(new ActionEvent(action, ACTION_ADD));
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) ->
                        dialog.dismiss()
                )
                .create();
    }
}