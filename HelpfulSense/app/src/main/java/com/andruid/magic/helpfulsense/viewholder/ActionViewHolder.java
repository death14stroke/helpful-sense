package com.andruid.magic.helpfulsense.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.andruid.magic.helpfulsense.databinding.LayoutActionBinding;
import com.andruid.magic.helpfulsense.model.Action;

public class ActionViewHolder extends RecyclerView.ViewHolder {
    private LayoutActionBinding binding;

    public ActionViewHolder(LayoutActionBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindAction(Action action){
        binding.setAction(action);
        binding.executePendingBindings();
    }
}