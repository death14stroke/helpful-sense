package com.andruid.magic.helpfulsense.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andruid.magic.helpfulsense.model.Action;
import com.andruid.magic.helpfulsense.util.FileUtil;

import java.util.List;

import timber.log.Timber;

public class ActionRepository {
    private static final Object LOCK = new Object();
    private static ActionRepository sInstance;

    public static ActionRepository getInstance() {
        if(sInstance == null){
            synchronized (LOCK) {
                Timber.tag("viewlog").d("Created repository instance");
                sInstance = new ActionRepository();
            }
        }
        return sInstance;
    }

    public MutableLiveData<List<Action>> getActionsFromFile(Context context){
        MutableLiveData<List<Action>> liveData = new MutableLiveData<>();
        liveData.setValue(FileUtil.readActionsFromFile(context));
        return liveData;
    }

    public void saveActionsToFile(Context context, List<Action> actions){
        FileUtil.writeActionsToFile(context, actions);
    }
}