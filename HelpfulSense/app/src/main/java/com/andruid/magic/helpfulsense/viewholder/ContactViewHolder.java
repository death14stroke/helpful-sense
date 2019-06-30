package com.andruid.magic.helpfulsense.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.andruid.magic.helpfulsense.databinding.LayoutContactBinding;
import com.wafflecopter.multicontactpicker.ContactResult;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    private LayoutContactBinding binding;

    public ContactViewHolder(LayoutContactBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindContact(ContactResult contactResult){
        binding.setContact(contactResult);
        binding.executePendingBindings();
    }
}