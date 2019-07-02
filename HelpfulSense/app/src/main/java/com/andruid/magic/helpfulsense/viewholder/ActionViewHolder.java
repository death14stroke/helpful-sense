package com.andruid.magic.helpfulsense.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.andruid.magic.helpfulsense.R;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class ActionViewHolder extends FlexibleViewHolder {
    public TextView actionTV;
    public ImageButton sendBtn;
    private View frontView, rearLeftView, rearRightView;

    public ActionViewHolder(View view, FlexibleAdapter adapter) {
        super(view, adapter);
        actionTV = view.findViewById(R.id.actionTV);
        sendBtn = view.findViewById(R.id.sendBtn);
        frontView = view.findViewById(R.id.frontView);
        rearLeftView = view.findViewById(R.id.rear_left_view);
        rearRightView = view.findViewById(R.id.rear_right_view);
    }

    @Override
    public View getFrontView() {
        return frontView;
    }

    @Override
    public View getRearLeftView() {
        return rearLeftView;
    }

    @Override
    public View getRearRightView() {
        return rearRightView;
    }
}