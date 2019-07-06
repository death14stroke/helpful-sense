package com.andruid.magic.helpfulsense.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.activity.IntroActivity;
import com.andruid.magic.helpfulsense.databinding.FragmentMessageBinding;
import com.andruid.magic.helpfulsense.util.DialogUtil;
import com.andruid.magic.helpfulsense.util.IntentUtil;

import java.util.Objects;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
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
        binding.sendBtn.setOnClickListener(v ->
                MessageFragmentPermissionsDispatcher.sendSMSWithPermissionCheck(this)
        );
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MessageFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.SEND_SMS)
    void sendSMS() {
        String message = Objects.requireNonNull(binding.messageET.getText()).toString().trim();
        Intent intent = IntentUtil.buildServiceSmsIntent(getContext(), message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            Objects.requireNonNull(getContext()).startForegroundService(intent);
        else
            Objects.requireNonNull(getContext()).startService(intent);
    }

    @OnShowRationale(Manifest.permission.SEND_SMS)
    void showRationale(PermissionRequest request){
        DialogUtil.buildInfoDialog(getContext(), getString(R.string.sms_permission), request)
                .show();
    }

    @OnPermissionDenied(Manifest.permission.SEND_SMS)
    void showDenied(){
        Toast.makeText(getContext(), "Denied permission", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.SEND_SMS)
    void showSettingsDialog(){
        DialogUtil.buildSettingsDialog(getContext(), getString(R.string.sms_permission))
                .show();
    }
}