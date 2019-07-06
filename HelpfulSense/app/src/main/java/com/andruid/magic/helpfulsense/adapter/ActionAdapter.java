package com.andruid.magic.helpfulsense.adapter;

import com.andruid.magic.helpfulsense.model.Action;
import com.andruid.magic.helpfulsense.model.ActionHolder;
import com.andruid.magic.helpfulsense.util.HolderUtil;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class ActionAdapter extends FlexibleAdapter<ActionHolder> {
    private SwipeListener mListener;

    public ActionAdapter(List<Action> actions, SwipeListener mListener) {
        super(HolderUtil.getActionHoldersFromActions(actions));
        this.mListener = mListener;
    }

    @Override
    public void onItemSwiped(int position, int direction) {
        super.onItemSwiped(position, direction);
        mListener.onSwipe(position, direction);
    }

    public void release() {
        mListener = null;
    }

    public interface SwipeListener {
        void onSwipe(int position, int direction);
    }
}