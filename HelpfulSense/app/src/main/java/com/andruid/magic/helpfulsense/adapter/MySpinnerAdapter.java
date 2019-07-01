package com.andruid.magic.helpfulsense.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.andruid.magic.helpfulsense.databinding.LayoutSpinnerBinding;
import com.andruid.magic.helpfulsense.model.Category;

import java.util.List;
import java.util.Objects;

public class MySpinnerAdapter extends BaseAdapter {
    private List<Category> categories;

    public MySpinnerAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutSpinnerBinding binding;
        if(convertView == null) {
            binding = LayoutSpinnerBinding.inflate(LayoutInflater.from(
                    parent.getContext()), parent, false);
            convertView = binding.getRoot();
        }
        else
            binding = DataBindingUtil.findBinding(convertView);
        Objects.requireNonNull(binding).setCategory(categories.get(position));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}