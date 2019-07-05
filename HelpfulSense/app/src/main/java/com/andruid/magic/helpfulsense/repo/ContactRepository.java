package com.andruid.magic.helpfulsense.repo;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.andruid.magic.helpfulsense.util.FileUtil;
import com.wafflecopter.multicontactpicker.ContactResult;

import java.util.List;

import timber.log.Timber;

public class ContactRepository {
    private static final Object LOCK = new Object();
    private static ContactRepository sInstance;

    public static ContactRepository getInstance() {
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new ContactRepository();
                Timber.tag("viewlog").d("Created contact repository instance");
            }
        }
        return sInstance;
    }

    public MutableLiveData<List<ContactResult>> getContactsFromFile(Context context){
        MutableLiveData<List<ContactResult>> liveData = new MutableLiveData<>();
        liveData.setValue(FileUtil.readContactsFromFile(context));
        return liveData;
    }

    public void saveContactsToFile(Context context, List<ContactResult> contacts){
        FileUtil.writeContactsToFile(context, contacts);
    }
}