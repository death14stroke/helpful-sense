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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.activity.IntroActivity;
import com.andruid.magic.helpfulsense.adapter.ActionAdapter;
import com.andruid.magic.helpfulsense.databinding.FragmentAlertBinding;
import com.andruid.magic.helpfulsense.eventbus.ActionEvent;
import com.andruid.magic.helpfulsense.model.Action;
import com.andruid.magic.helpfulsense.model.ActionHolder;
import com.andruid.magic.helpfulsense.service.SensorService;
import com.andruid.magic.helpfulsense.util.HolderUtil;
import com.andruid.magic.helpfulsense.viewmodel.ActionViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;

import eu.davidea.flexibleadapter.helpers.EmptyViewHelper;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

import static com.andruid.magic.helpfulsense.data.Constants.ACTION_ADD;
import static com.andruid.magic.helpfulsense.data.Constants.ACTION_EDIT;
import static com.andruid.magic.helpfulsense.data.Constants.ACTION_SMS;
import static com.andruid.magic.helpfulsense.data.Constants.ACTION_SWIPE;
import static com.andruid.magic.helpfulsense.data.Constants.INTENT_LOC_SMS;
import static com.andruid.magic.helpfulsense.data.Constants.KEY_MESSAGE;

@RuntimePermissions
public class AlertFragment extends Fragment implements ActionAdapter.SwipeListener {
    private FragmentAlertBinding binding;
    private ActionAdapter actionAdapter;
    private ActionViewModel actionViewModel;
    private int swipedPos;

    public static AlertFragment newInstance() {
        return new AlertFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
        actionViewModel  = ViewModelProviders.of(this).get(ActionViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alert, container,
                false);
        binding.emptyLayout.emptyView.setOnClickListener(v ->
                openAddActionDialog()
        );
        setUpViewModel();
        return binding.getRoot();
    }

    private void setUpViewModel() {
        actionViewModel.getSavedActions().observe(this, actions -> {
            actionAdapter = new ActionAdapter(actions, AlertFragment.this);
            binding.recyclerView.setAdapter(actionAdapter);
            actionAdapter.setLongPressDragEnabled(true)
                    .setSwipeEnabled(true);
            EmptyViewHelper.create(actionAdapter, binding.emptyLayout.emptyView);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActionEvent(ActionEvent actionEvent){
        Action action = actionEvent.getAction();
        String command = actionEvent.getCommand();
        if(ACTION_ADD.equals(command))
            actionAdapter.addItem(new ActionHolder(action));
        else if(ACTION_EDIT.equals(command))
            actionAdapter.updateItem(swipedPos, new ActionHolder(action), null);
        else if(ACTION_SMS.equals(command))
            AlertFragmentPermissionsDispatcher.sendSMSWithPermissionCheck(this, action);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AlertFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_action, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_action:
                openAddActionDialog();
                break;
            case R.id.menu_help:
                startActivity(new Intent(getContext(), IntroActivity.class));
                break;
        }
        return true;
    }

    private void openAddActionDialog() {
        DialogFragment dialogFragment = ActionDialogFragment.newInstance(ACTION_ADD, null);
        dialogFragment.show(getChildFragmentManager(), getString(R.string.add_action));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        List<Action> actions = HolderUtil.getActionsFromActionHolders(actionAdapter.getCurrentItems());
        if(!actions.isEmpty())
            actionViewModel.updateSavedActions(actions);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSwipe(int position, int direction) {
        Timber.tag("viewlog").d("swiped: %d", position);
        swipedPos = position;
        if(direction == ItemTouchHelper.LEFT || position == ItemTouchHelper.START) {
            actionAdapter.removeItem(position);
            Timber.tag("viewlog").d("dir: left");
        } else {
            Timber.tag("viewlog").d("dir: right");
            DialogFragment dialogFragment = ActionDialogFragment.newInstance(ACTION_SWIPE,
                    Objects.requireNonNull(actionAdapter.getItem(position)).getAction());
            dialogFragment.show(getChildFragmentManager(), getString(R.string.add_action));
        }
    }

    @NeedsPermission(Manifest.permission.SEND_SMS)
    void sendSMS(Action action) {
        Intent intent = new Intent(getContext(), SensorService.class);
        intent.setAction(INTENT_LOC_SMS);
        intent.putExtra(KEY_MESSAGE, action.getMessage());
    }

    @OnShowRationale(Manifest.permission.SEND_SMS)
    void showRationale(PermissionRequest request){
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setMessage("Send emergency SMS to your selected trusted people. Grant SMS permission " +
                        "for the same")
                .setPositiveButton("Allow", (dialog, which) -> request.proceed())
                .setNegativeButton("Deny", (dialog, which) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.SEND_SMS)
    void showDenied(){
        Toast.makeText(getContext(), "Denied", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.SEND_SMS)
    void showSettingsDialog(){
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setMessage("Send emergency SMS to your selected trusted people")
                .setPositiveButton("Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Deny", (dialog, which) -> dialog.dismiss())
                .show();
    }
}