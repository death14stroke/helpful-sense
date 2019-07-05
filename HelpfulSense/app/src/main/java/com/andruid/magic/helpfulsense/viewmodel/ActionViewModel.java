package com.andruid.magic.helpfulsense.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andruid.magic.helpfulsense.model.Action;
import com.andruid.magic.helpfulsense.repo.ActionRepository;
import com.andruid.magic.helpfulsense.service.ActionIntentService;

import java.util.List;

import timber.log.Timber;

public class ActionViewModel extends AndroidViewModel {
    private MutableLiveData<List<Action>> actionLiveData;
    private Application application;

    public ActionViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        Timber.tag("viewlog").d("created view model");
        actionLiveData = ActionRepository.getInstance()
                .getActionsFromFile(application.getApplicationContext());
    }

    public LiveData<List<Action>> getSavedActions() {
        Timber.tag("viewlog").d("get live data from view model");
        return actionLiveData;
    }

    public void updateSavedActions(List<Action> actions){
        actionLiveData.setValue(actions);
        ActionIntentService.startActionUpdate(application, actions);
    }
}