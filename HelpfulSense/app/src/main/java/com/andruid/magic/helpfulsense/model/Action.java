package com.andruid.magic.helpfulsense.model;

import android.os.Parcel;
import android.os.Parcelable;
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

public class Action extends AbstractFlexibleItem<ActionViewHolder> implements Parcelable {
    private final String message;
    private final Category category;

    public Action(String message, Category category) {
        this.message = message;
        this.category = category;
    }

    protected Action(Parcel in) {
        message = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
    }

    public static final Creator<Action> CREATOR = new Creator<Action>() {
        @Override
        public Action createFromParcel(Parcel in) {
            return new Action(in);
        }

        @Override
        public Action[] newArray(int size) {
            return new Action[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public Category getCategory() {
        return category;
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
        holder.getFrontView().setBackgroundResource(category.getBgColor());
        holder.sendBtn.setOnClickListener(v ->
                EventBus.getDefault().post(new ActionEvent(this, ACTION_SMS))
        );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeParcelable(category, flags);
    }
}