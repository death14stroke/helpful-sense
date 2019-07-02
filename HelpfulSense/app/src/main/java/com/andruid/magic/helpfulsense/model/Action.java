package com.andruid.magic.helpfulsense.model;

import android.view.View;

import androidx.core.content.ContextCompat;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.eventbus.ActionEvent;
import com.andruid.magic.helpfulsense.viewholder.ActionViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;

import static com.andruid.magic.helpfulsense.data.Constants.ACTION_SMS;

public class Action extends AbstractFlexibleItem<ActionViewHolder> {
    private final String message;
    private final Category category;

    public Action(String message, Category category) {
        this.message = message;
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || o.getClass() != this.getClass())
            return false;
        Action a = (Action) o;
        return message.equals(a.message);
    }

    @Override
    public boolean isDraggable() {
        return true;
    }

    @Override
    public boolean isSwipeable() {
        return true;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_action;
    }

    @Override
    public ActionViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new ActionViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ActionViewHolder holder, int position, List<Object> payloads) {
        holder.actionTV.setText(message);
        holder.actionTV.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(
                holder.itemView.getContext(), category.getIcon()), null, null, null);
        holder.sendBtn.setOnClickListener(v ->
                EventBus.getDefault().post(new ActionEvent(this, ACTION_SMS))
        );
    }
}