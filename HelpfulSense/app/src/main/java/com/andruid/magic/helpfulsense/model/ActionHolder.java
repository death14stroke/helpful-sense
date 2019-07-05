package com.andruid.magic.helpfulsense.model;

import android.view.View;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.viewholder.ActionViewHolder;

import java.util.List;
import java.util.Objects;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;

public class ActionHolder extends AbstractFlexibleItem<ActionViewHolder> {
    private Action action;

    public ActionHolder(Action action){
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || o.getClass() != getClass())
            return false;
        ActionHolder a = (ActionHolder) o;
        return action.equals(a.action);
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
        holder.bindAction(((ActionHolder) Objects.requireNonNull(adapter.getItem(position))).action);
    }
}