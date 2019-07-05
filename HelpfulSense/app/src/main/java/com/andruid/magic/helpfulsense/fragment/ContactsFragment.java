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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.activity.IntroActivity;
import com.andruid.magic.helpfulsense.databinding.FragmentContactsBinding;
import com.andruid.magic.helpfulsense.eventbus.ContactsEvent;
import com.andruid.magic.helpfulsense.model.ContactHolder;
import com.andruid.magic.helpfulsense.repo.ContactRepository;
import com.andruid.magic.helpfulsense.util.FileUtil;
import com.andruid.magic.helpfulsense.util.HolderUtil;
import com.andruid.magic.helpfulsense.viewmodel.ContactViewModel;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.helpers.EmptyViewHelper;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.andruid.magic.helpfulsense.data.Constants.CONTACTS_PICKER_REQUEST;
import static com.andruid.magic.helpfulsense.data.Constants.MAX_CONTACTS;

@RuntimePermissions
public class ContactsFragment extends Fragment {
    private FragmentContactsBinding binding;
    private ContactViewModel contactViewModel;
    private FlexibleAdapter<ContactHolder> contactsAdapter;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container,
                false);
        binding.emptyLayout.textView.setText(getString(R.string.empty_contacts));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(
                getContext()), DividerItemDecoration.VERTICAL));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        setUpViewModel();
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_contacts, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_edit_contacts:
                ContactsFragmentPermissionsDispatcher.openContactsPickerWithPermissionCheck(this);
                break;
            case R.id.menu_help:
                startActivity(new Intent(getContext(), IntroActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ContactsFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        List<ContactResult> contacts = HolderUtil.getContactsFromContactHolders(contactsAdapter.getCurrentItems());
        if(!contacts.isEmpty())
            contactViewModel.updateSavedContacts(contacts);
        EventBus.getDefault().unregister(this);
    }

    private void setUpViewModel(){
        contactViewModel.getSavedContacts().observe(this, contacts -> {
            contactsAdapter = new FlexibleAdapter<>(HolderUtil
                    .getContactHoldersFromContacts(contacts));
            binding.recyclerView.setAdapter(contactsAdapter);
            EmptyViewHelper.create(contactsAdapter, binding.emptyLayout.emptyView);
            contactsAdapter.setLongPressDragEnabled(true);
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onContactsEvent(ContactsEvent contactsEvent){
        List<ContactResult> results = contactsEvent.getResults();
        contactViewModel.updateSavedContacts(results);
        ContactRepository.getInstance().saveContactsToFile(getContext(), results);
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    void openContactsPicker() {
        new MultiContactPicker.Builder(Objects.requireNonNull(getActivity()))
                .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out)
                .limitToColumn(LimitColumn.PHONE)
                .setSelectedContacts((ArrayList<ContactResult>) FileUtil.readContactsFromFile(
                        Objects.requireNonNull(getContext())))
                .setSelectionLimit(MAX_CONTACTS)
                .showPickerForResult(CONTACTS_PICKER_REQUEST);
    }

    @OnShowRationale(Manifest.permission.READ_CONTACTS)
    void showRationale(PermissionRequest request){
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setMessage("Select your most trusted contacts who will receive your emergency texts. " +
                        "Grant contacts permission for the same.")
                .setPositiveButton("Allow", (dialog, which) -> request.proceed())
                .setNegativeButton("Deny", (dialog, which) -> request.cancel())
                .show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_CONTACTS)
    void showSettingsDialog(){
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setMessage("Select your most trusted contacts who will receive your emergency texts")
                .setPositiveButton("Settings", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Deny", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.READ_CONTACTS)
    void showDenied(){
        Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
    }
}