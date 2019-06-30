package com.andruid.magic.helpfulsense.eventbus;

import com.wafflecopter.multicontactpicker.ContactResult;

import java.util.List;

public class ContactsEvent {
    private final List<ContactResult> results;

    public ContactsEvent(List<ContactResult> results) {
        this.results = results;
    }

    public List<ContactResult> getResults() {
        return results;
    }
}