package com.andruid.magic.helpfulsense.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.andruid.magic.helpfulsense.model.Action;
import com.andruid.magic.helpfulsense.repo.ActionRepository;
import com.andruid.magic.helpfulsense.repo.ContactRepository;
import com.wafflecopter.multicontactpicker.ContactResult;

import java.util.ArrayList;
import java.util.List;

public class UpdateIntentService extends IntentService {
    private static final String INTENT_CONTACT_UPDATE = "intent_contact_update",
            KEY_CONTACTS = "key_contacts";
    private static final String INTENT_ACTION_UPDATE = "intent_action_update",
            KEY_ACTIONS = "key_actions";

    public UpdateIntentService() {
        super("UpdateIntentService");
    }

    public static void startActionUpdate(Context context, List<Action> actions) {
        Intent intent = new Intent(context, UpdateIntentService.class);
        intent.setAction(INTENT_ACTION_UPDATE);
        intent.putParcelableArrayListExtra(KEY_ACTIONS, (ArrayList<? extends Parcelable>) actions);
        context.startService(intent);
    }

    public static void startContactUpdate(Context context, List<ContactResult> contacts) {
        Intent intent = new Intent(context, UpdateIntentService.class);
        intent.setAction(INTENT_CONTACT_UPDATE);
        intent.putParcelableArrayListExtra(KEY_CONTACTS, (ArrayList<? extends Parcelable>) contacts);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (INTENT_ACTION_UPDATE.equals(action)) {
                List<Action> actions = intent.getParcelableArrayListExtra(KEY_ACTIONS);
                ActionRepository.getInstance().saveActionsToFile(this, actions);
            }
            else if(INTENT_CONTACT_UPDATE.equals(action)){
                List<ContactResult> contacts = intent.getParcelableArrayListExtra(KEY_CONTACTS);
                ContactRepository.getInstance().saveContactsToFile(this, contacts);
            }
        }
    }
}