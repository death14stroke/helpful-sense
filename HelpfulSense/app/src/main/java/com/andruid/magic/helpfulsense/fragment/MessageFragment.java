package com.andruid.magic.helpfulsense.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.databinding.FragmentMessageBinding;
import com.andruid.magic.helpfulsense.service.SensorService;

import java.util.Objects;

import static com.andruid.magic.helpfulsense.data.Constants.INTENT_LOC_SMS;
import static com.andruid.magic.helpfulsense.data.Constants.KEY_MESSAGE;

public class MessageFragment extends Fragment {
    private FragmentMessageBinding binding;

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container,
                false);
        binding.sendBtn.setOnClickListener(v -> sendSMS());
        return binding.getRoot();
    }

    private void sendSMS() {
        String message = Objects.requireNonNull(binding.messageET.getText()).toString().trim();
        Intent intent = new Intent(getContext(), SensorService.class);
        intent.setAction(INTENT_LOC_SMS);
        intent.putExtra(KEY_MESSAGE, message);
    }
}