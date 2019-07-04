package com.andruid.magic.helpfulsense.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andruid.magic.helpfulsense.databinding.LayoutContactBinding;
import com.andruid.magic.helpfulsense.viewholder.ContactViewHolder;
import com.wafflecopter.multicontactpicker.ContactResult;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    private List<ContactResult> contacts;

    public ContactAdapter(List<ContactResult> contacts){
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutContactBinding binding = LayoutContactBinding.inflate(LayoutInflater.from(
                parent.getContext()));
        return new ContactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactResult contact = contacts.get(position);
        holder.bindContact(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}