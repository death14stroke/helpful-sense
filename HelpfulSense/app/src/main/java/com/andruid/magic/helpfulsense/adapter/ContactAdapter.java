package com.andruid.magic.helpfulsense.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andruid.magic.helpfulsense.databinding.LayoutContactBinding;
import com.andruid.magic.helpfulsense.model.ContactHolder;
import com.andruid.magic.helpfulsense.util.HolderUtil;
import com.andruid.magic.helpfulsense.viewholder.ContactViewHolder;
import com.wafflecopter.multicontactpicker.ContactResult;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class ContactAdapter extends FlexibleAdapter<ContactHolder> {
    public ContactAdapter(List<ContactResult> contacts){
        super(HolderUtil.getContactHoldersFromContacts(contacts));
    }
}