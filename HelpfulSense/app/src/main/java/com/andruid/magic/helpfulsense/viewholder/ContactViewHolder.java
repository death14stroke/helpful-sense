package com.andruid.magic.helpfulsense.viewholder;

import android.view.View;

import com.andruid.magic.helpfulsense.databinding.LayoutContactBinding;
import com.wafflecopter.multicontactpicker.ContactResult;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class ContactViewHolder extends FlexibleViewHolder {
    private LayoutContactBinding binding;

    public ContactViewHolder(View view, FlexibleAdapter adapter) {
        super(view, adapter);
        binding = LayoutContactBinding.bind(view);
    }

    public void bindContact(ContactResult contactResult){
        binding.setContact(contactResult);
        binding.executePendingBindings();
    }

    @Override
    public View getFrontView() {
        return binding.frontView;
    }
}