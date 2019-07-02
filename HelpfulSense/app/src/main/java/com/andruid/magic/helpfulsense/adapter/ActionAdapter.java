package com.andruid.magic.helpfulsense.adapter;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.andruid.magic.helpfulsense.model.Action;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class ActionAdapter extends FlexibleAdapter<Action> {
    private Context context;

    public ActionAdapter(Context context, @Nullable List<Action> items) {
        super(items);
        this.context = context;
    }

    @Override
    public void onItemSwiped(int position, int direction) {
        super.onItemSwiped(position, direction);
        if(position == ItemTouchHelper.LEFT || position == ItemTouchHelper.START)
            Toast.makeText(context, "swipe left:"+position, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "swipe right:"+position, Toast.LENGTH_SHORT).show();
    }
}