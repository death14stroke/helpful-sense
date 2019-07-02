package com.andruid.magic.helpfulsense.fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.databinding.FragmentAlertBinding;
import com.andruid.magic.helpfulsense.eventbus.ActionEvent;
import com.andruid.magic.helpfulsense.model.Action;
import com.andruid.magic.helpfulsense.service.SensorService;
import com.andruid.magic.helpfulsense.util.FileUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.andruid.magic.helpfulsense.data.Constants.ACTION_ADD;
import static com.andruid.magic.helpfulsense.data.Constants.ACTION_SMS;
import static com.andruid.magic.helpfulsense.data.Constants.INTENT_LOC_SMS;
import static com.andruid.magic.helpfulsense.data.Constants.KEY_MESSAGE;

@RuntimePermissions
public class AlertFragment extends Fragment {
    private FragmentAlertBinding binding;
    private FlexibleAdapter<Action> actionAdapter;

    public static AlertFragment newInstance() {
        return new AlertFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alert, container,
                false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadActions();
        return binding.getRoot();
    }

    private void loadActions() {
        List<Action> actionList = FileUtil.readActionsFromFile(Objects.requireNonNull(
                getContext()));
        actionAdapter = new FlexibleAdapter<>(actionList);
        binding.recyclerView.setAdapter(actionAdapter);
        actionAdapter.setLongPressDragEnabled(true)
                .setSwipeEnabled(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActionEvent(ActionEvent actionEvent){
        Action action = actionEvent.getAction();
        String command = actionEvent.getCommand();
        if(ACTION_ADD.equals(command))
            actionAdapter.addItem(action);
        else if(ACTION_SMS.equals(command)){
            AlertFragmentPermissionsDispatcher.sendSMSWithPermissionCheck(this, action);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AlertFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_action, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_add_action){
            DialogFragment dialogFragment = new ActionDialogFragment();
            dialogFragment.show(getChildFragmentManager(), "add_action");
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        List<Action> actions = actionAdapter.getCurrentItems();
        if(!actions.isEmpty())
            FileUtil.writeActionsToFile(Objects.requireNonNull(getContext()), actions);
    }

    @NeedsPermission(Manifest.permission.SEND_SMS)
    public void sendSMS(Action action) {
        Intent intent = new Intent(getContext(), SensorService.class);
        intent.setAction(INTENT_LOC_SMS);
        intent.putExtra(KEY_MESSAGE, action.getMessage());
    }

    @OnShowRationale(Manifest.permission.SEND_SMS)
    public void showRationale(PermissionRequest request){
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setMessage("Send emergency SMS to your selected trusted people. Grant SMS permission " +
                        "for the same")
                .setPositiveButton("Allow", (dialog, which) -> request.proceed())
                .setNegativeButton("Deny", (dialog, which) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.SEND_SMS)
    public void showDenied(){
        Toast.makeText(getContext(), "Denied", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.SEND_SMS)
    public void showSettingsDialog(){
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setMessage("Send emergency SMS to your selected trusted people")
                .setPositiveButton("Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Deny", null)
                .show();
    }
}