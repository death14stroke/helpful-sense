package com.andruid.magic.helpfulsense.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andruid.magic.helpfulsense.repo.ContactRepository;
import com.andruid.magic.helpfulsense.service.UpdateIntentService;
import com.wafflecopter.multicontactpicker.ContactResult;

import java.util.List;

import timber.log.Timber;

public class ContactViewModel extends AndroidViewModel {
    private MutableLiveData<List<ContactResult>> contactLiveData;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        Timber.d("created view model");
        contactLiveData = ContactRepository.getInstance()
                .getContactsFromFile(application.getApplicationContext());
    }

    public LiveData<List<ContactResult>> getSavedContacts() {
        return contactLiveData;
    }

    public void updateSavedContacts(List<ContactResult> contacts){
        contactLiveData.setValue(contacts);
        UpdateIntentService.startContactUpdate(getApplication(), contacts);
    }
}