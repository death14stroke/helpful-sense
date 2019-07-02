package com.andruid.magic.helpfulsense.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.adapter.ActionAdapter;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import timber.log.Timber;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {
    private ActionAdapter actionAdapter;
    private Drawable icon;
    private final ColorDrawable background;
    private Context context;

    public SwipeCallback(Context context, ActionAdapter actionAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END);
        this.actionAdapter = actionAdapter;
        this.context = context;
        icon = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_delete);
        background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.START)
            Timber.tag("swipelog").d("swipe start: %s", position);
        else if(direction == ItemTouchHelper.END)
            Timber.tag("swipelog").d("swipe end: %s", position);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(context, c, recyclerView, viewHolder, dX, dY,
                actionState, isCurrentlyActive)
                .addSwipeRightActionIcon(android.R.drawable.ic_menu_edit)
                .addSwipeRightBackgroundColor(ContextCompat.getColor(context, R.color.colorTab2))
                .addSwipeLeftActionIcon(android.R.drawable.ic_menu_delete)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}