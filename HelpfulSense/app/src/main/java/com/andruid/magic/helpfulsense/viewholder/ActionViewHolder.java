package com.andruid.magic.helpfulsense.viewholder;

import android.view.View;

import androidx.core.content.ContextCompat;

import com.andruid.magic.helpfulsense.databinding.LayoutActionBinding;
import com.andruid.magic.helpfulsense.eventbus.ActionEvent;
import com.andruid.magic.helpfulsense.model.Action;

import org.greenrobot.eventbus.EventBus;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

import static com.andruid.magic.helpfulsense.data.Constants.ACTION_SMS;

public class ActionViewHolder extends FlexibleViewHolder {
    private LayoutActionBinding binding;

    public ActionViewHolder(View view, FlexibleAdapter adapter) {
        super(view, adapter);
        binding = LayoutActionBinding.bind(view);
    }

    public void bindAction(Action action){
        binding.setAction(action);
        binding.actionTV.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(
                binding.frontView.getContext(), action.getCategory().getIcon()), null, null, null);
        binding.frontView.setBackgroundResource(action.getCategory().getIconColor());
        binding.sendBtn.setOnClickListener(v ->
                EventBus.getDefault().post(new ActionEvent(action, ACTION_SMS))
        );
        binding.executePendingBindings();
    }

    @Override
    public View getFrontView() {
        return binding.frontView;
    }

    @Override
    public View getRearLeftView() {
        return binding.rearLeftView;
    }

    @Override
    public View getRearRightView() {
        return binding.rearRightView;
    }
}