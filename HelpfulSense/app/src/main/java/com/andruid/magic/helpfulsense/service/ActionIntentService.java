package com.andruid.magic.helpfulsense.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Parcelable;

import com.andruid.magic.helpfulsense.model.Action;
import com.andruid.magic.helpfulsense.repo.ActionRepository;

import java.util.ArrayList;
import java.util.List;

import static com.andruid.magic.helpfulsense.data.Constants.INTENT_ACTION_UPDATE;
import static com.andruid.magic.helpfulsense.data.Constants.KEY_ACTION;

public class ActionIntentService extends IntentService {

    public ActionIntentService() {
        super("ActionIntentService");
    }

    public static void startActionUpdate(Context context, List<Action> actions) {
        Intent intent = new Intent(context, ActionIntentService.class);
        intent.setAction(INTENT_ACTION_UPDATE);
        intent.putParcelableArrayListExtra(KEY_ACTION, (ArrayList<? extends Parcelable>) actions);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (INTENT_ACTION_UPDATE.equals(action)) {
                List<Action> actions = intent.getParcelableArrayListExtra(KEY_ACTION);
                ActionRepository.getInstance().saveActionsToFile(this, actions);
            }
        }
    }
}