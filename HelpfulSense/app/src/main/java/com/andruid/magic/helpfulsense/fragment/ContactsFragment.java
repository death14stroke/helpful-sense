package com.andruid.magic.helpfulsense.fragment;

import android.Manifest;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.adapter.ContactAdapter;
import com.andruid.magic.helpfulsense.databinding.FragmentContactsBinding;
import com.andruid.magic.helpfulsense.eventbus.ContactsEvent;
import com.andruid.magic.helpfulsense.util.FileUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.andruid.magic.helpfulsense.data.Constants.CONTACTS_PICKER_REQUEST;

public class ContactsFragment extends Fragment implements PermissionListener {
    private FragmentContactsBinding binding;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacts, container,
                false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(
                getContext()), DividerItemDecoration.VERTICAL));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.swipeRefresh.setRefreshing(true);
        binding.swipeRefresh.setOnRefreshListener(this::loadContacts);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_contacts, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_edit_contacts)
            openContactsPicker();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadContacts();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void openContactsPicker() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(this)
                .check();
    }

    private void loadContacts(){
        List<ContactResult> contacts = FileUtil.readContactsFromFile(Objects.requireNonNull(
                getContext()));
        ContactAdapter contactAdapter = new ContactAdapter(contacts);
        binding.recyclerView.setAdapter(contactAdapter);
        binding.swipeRefresh.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onContactsEvent(ContactsEvent contactsEvent){
        List<ContactResult> results = contactsEvent.getResults();
        FileUtil.writeContactsToFile(Objects.requireNonNull(getContext()), results);
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        if(response.getPermissionName().equals(Manifest.permission.READ_CONTACTS))
            new MultiContactPicker.Builder(Objects.requireNonNull(getActivity()))
                    .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                            android.R.anim.fade_in, android.R.anim.fade_out)
                    .limitToColumn(LimitColumn.PHONE)
                    .setSelectedContacts((ArrayList<ContactResult>) FileUtil.readContactsFromFile(
                            Objects.requireNonNull(getContext())))
                    .showPickerForResult(CONTACTS_PICKER_REQUEST);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        Toast.makeText(getContext(), response.getPermissionName()+" denied", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        token.continuePermissionRequest();
    }
}