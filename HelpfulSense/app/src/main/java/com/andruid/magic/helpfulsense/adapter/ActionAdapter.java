package com.andruid.magic.helpfulsense.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andruid.magic.helpfulsense.databinding.LayoutActionBinding;
import com.andruid.magic.helpfulsense.model.Action;
import com.andruid.magic.helpfulsense.viewholder.ActionViewHolder;

import java.util.List;

public class ActionAdapter extends RecyclerView.Adapter<ActionViewHolder> {
    public List<Action> getActions() {
        return actions;
    }

    private List<Action> actions;

    public ActionAdapter(List<Action> actions){
        this.actions = actions;
    }

    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutActionBinding binding = LayoutActionBinding.inflate(LayoutInflater.from(
                parent.getContext()), parent, false);
        //return new ActionViewHolder(binding);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        Action action = actions.get(position);
        //holder.bindAction(action);
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }
}