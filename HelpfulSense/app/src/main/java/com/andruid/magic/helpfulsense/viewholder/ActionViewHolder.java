package com.andruid.magic.helpfulsense.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.andruid.magic.helpfulsense.databinding.LayoutActionBinding;
import com.andruid.magic.helpfulsense.eventbus.ActionEvent;
import com.andruid.magic.helpfulsense.model.Action;

import org.greenrobot.eventbus.EventBus;

import static com.andruid.magic.helpfulsense.data.Constants.ACTION_SMS;

public class ActionViewHolder extends RecyclerView.ViewHolder {
    private LayoutActionBinding binding;

    public ActionViewHolder(LayoutActionBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindAction(Action action){
        binding.sendBtn.setOnClickListener(v ->
                EventBus.getDefault().post(new ActionEvent(action, ACTION_SMS))
        );
        binding.setAction(action);
        binding.executePendingBindings();
    }
}