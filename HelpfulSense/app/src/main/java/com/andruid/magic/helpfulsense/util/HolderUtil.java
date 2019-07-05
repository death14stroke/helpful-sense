package com.andruid.magic.helpfulsense.util;

import com.andruid.magic.helpfulsense.model.Action;
import com.andruid.magic.helpfulsense.model.ActionHolder;
import com.andruid.magic.helpfulsense.model.ContactHolder;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.RxContacts.Contact;

import java.util.ArrayList;
import java.util.List;

public class HolderUtil {
    public static List<ActionHolder> getActionHoldersFromActions(List<Action> actions){
        List<ActionHolder> actionHolderList = new ArrayList<>();
        for(Action action : actions)
            actionHolderList.add(new ActionHolder(action));
        return actionHolderList;
    }

    public static List<Action> getActionsFromActionHolders(List<ActionHolder> actionHolders){
        List<Action> actions = new ArrayList<>();
        for(ActionHolder actionHolder : actionHolders)
            actions.add(actionHolder.getAction());
        return actions;
    }

    public static List<ContactHolder> getContactHoldersFromContacts(List<ContactResult> contacts){
        List<ContactHolder> contactHolderList = new ArrayList<>();
        for(ContactResult contact : contacts)
            contactHolderList.add(new ContactHolder(contact));
        return contactHolderList;
    }

    public static List<ContactResult> getContactsFromContactHolders(List<ContactHolder> contactHolders){
        List<ContactResult> contacts = new ArrayList<>();
        for(ContactHolder contactHolder : contactHolders)
            contacts.add(contactHolder.getContact());
        return contacts;
    }
}