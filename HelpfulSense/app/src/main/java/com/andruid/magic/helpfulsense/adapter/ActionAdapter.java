package com.andruid.magic.helpfulsense.adapter;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.andruid.magic.helpfulsense.eventbus.ActionEvent;
import com.andruid.magic.helpfulsense.fragment.ActionDialogFragment;
import com.andruid.magic.helpfulsense.model.Action;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

import static com.andruid.magic.helpfulsense.data.Constants.ACTION_EDIT;
import static com.andruid.magic.helpfulsense.data.Constants.ACTION_SWIPE;

public class ActionAdapter extends FlexibleAdapter<Action> {
    private Context context;

    public ActionAdapter(Context context, @Nullable List<Action> items) {
        super(items);
        this.context = context;
    }

    @Override
    public void onItemSwiped(int position, int direction) {
        super.onItemSwiped(position, direction);
        if(direction == ItemTouchHelper.LEFT || position == ItemTouchHelper.START)
            removeItem(position);
        else
            EventBus.getDefault().post(new ActionEvent(getItem(position), ACTION_SWIPE));
    }
}