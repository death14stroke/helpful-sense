package com.andruid.magic.helpfulsense.fragment;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.adapter.MySpinnerAdapter;
import com.andruid.magic.helpfulsense.databinding.DialogActionBinding;
import com.andruid.magic.helpfulsense.eventbus.ActionEvent;
import com.andruid.magic.helpfulsense.model.Action;
import com.andruid.magic.helpfulsense.model.Category;
import com.annimon.stream.IntStream;
import com.annimon.stream.OptionalInt;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static com.andruid.magic.helpfulsense.data.Constants.ACTION_EDIT;
import static com.andruid.magic.helpfulsense.data.Constants.ACTION_SWIPE;
import static com.andruid.magic.helpfulsense.data.Constants.KEY_ACTION;
import static com.andruid.magic.helpfulsense.data.Constants.KEY_COMMAND;

public class ActionDialogFragment extends DialogFragment {
    private DialogActionBinding binding;
    private MySpinnerAdapter mySpinnerAdapter;
    private String command;

    static ActionDialogFragment newInstance(String command, Action action){
        Bundle args = new Bundle();
        args.putString(KEY_COMMAND, command);
        args.putParcelable(KEY_ACTION, action);
        ActionDialogFragment dialogFragment = new ActionDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        String[] categoryNames = Objects.requireNonNull(getContext()).getResources()
                .getStringArray(R.array.category_names);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.category_icons);
        int[] icons = new int[typedArray.length()];
        for(int i=0; i<typedArray.length(); i++)
            icons[i] = typedArray.getResourceId(i, -1);
        typedArray.recycle();
        typedArray = getResources().obtainTypedArray(R.array.category_colors);
        int[] colors = new int[typedArray.length()];
        for(int i=0; i<typedArray.length(); i++)
            colors[i] = typedArray.getResourceId(i, -1);
        typedArray.recycle();
        List<Category> categories = new ArrayList<>();
        for(int i=0; i<icons.length; i++)
            categories.add(new Category(categoryNames[i], icons[i], colors[i]));
        mySpinnerAdapter = new MySpinnerAdapter(categories);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_action,
                null, false);
        binding.spinner.setAdapter(mySpinnerAdapter);
        Bundle args = getArguments();
        if(args != null){
            Action action = args.getParcelable(KEY_ACTION);
            command = args.getString(KEY_COMMAND);
            List<Category> categories = mySpinnerAdapter.getCategories();
            if(action != null) {
                binding.messageET.setText(action.getMessage());
                OptionalInt pos = IntStream.range(0, categories.size())
                        .filter(i -> action.getCategory().getName().equals(categories.get(i).getName()))
                        .findFirst();
                if(pos.isPresent())
                    binding.spinner.setSelection(pos.getAsInt());
            }
        }
        return new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setTitle(R.string.add_action)
                .setView(binding.getRoot())
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String message = binding.messageET.getText().toString().trim();
                    Category category = (Category) binding.spinner.getSelectedItem();
                    Timber.d("selected category %s", category.getName());
                    Action action = new Action(message, category);
                    if(ACTION_SWIPE.equals(command))
                        command = ACTION_EDIT;
                    EventBus.getDefault().post(new ActionEvent(action, command));
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) ->
                        dialog.dismiss()
                )
                .create();
    }
}