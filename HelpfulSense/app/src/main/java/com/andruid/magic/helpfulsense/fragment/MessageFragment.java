package com.andruid.magic.helpfulsense.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.activity.IntroActivity;
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container,
                false);
        binding.sendBtn.setOnClickListener(v -> sendSMS());
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_help, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_help)
            startActivity(new Intent(getContext(), IntroActivity.class));
        return true;
    }

    private void sendSMS() {
        String message = Objects.requireNonNull(binding.messageET.getText()).toString().trim();
        Intent intent = new Intent(getContext(), SensorService.class);
        intent.setAction(INTENT_LOC_SMS);
        intent.putExtra(KEY_MESSAGE, message);
    }
}