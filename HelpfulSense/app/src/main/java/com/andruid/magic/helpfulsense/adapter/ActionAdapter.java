package com.andruid.magic.helpfulsense.adapter;

import androidx.annotation.Nullable;

import com.andruid.magic.helpfulsense.model.Action;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class ActionAdapter extends FlexibleAdapter<Action> {
    private SwipeListener mListener;

    public ActionAdapter(@Nullable List<Action> items, SwipeListener mListener) {
        super(items);
        this.mListener = mListener;
    }

    @Override
    public void onItemSwiped(int position, int direction) {
        super.onItemSwiped(position, direction);
        mListener.onSwipe(position, direction);
    }

    public interface SwipeListener {
        void onSwipe(int position, int direction);
    }
}