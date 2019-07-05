package com.andruid.magic.helpfulsense.model;

import android.view.View;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.viewholder.ContactViewHolder;
import com.wafflecopter.multicontactpicker.ContactResult;

import java.util.List;
import java.util.Objects;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;

public class ContactHolder extends AbstractFlexibleItem<ContactViewHolder> {
    private final ContactResult contact;

    public ContactHolder(ContactResult contact){
        this.contact = contact;
    }

    public ContactResult getContact() {
        return contact;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || o.getClass() != getClass())
            return false;
        ContactHolder c = (ContactHolder) o;
        return contact.equals(c.contact);
    }

    @Override
    public boolean isSwipeable() {
        return true;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_contact;
    }

    @Override
    public ContactViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ContactViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ContactViewHolder holder, int position, List<Object> payloads) {
        holder.bindContact(((ContactHolder) Objects.requireNonNull(adapter.getItem(position)))
                .contact);
    }
}