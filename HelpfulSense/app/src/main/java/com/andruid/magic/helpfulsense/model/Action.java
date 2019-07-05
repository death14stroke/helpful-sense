package com.andruid.magic.helpfulsense.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Action implements Parcelable {
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
    public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != getClass())
            return false;
        Action a = (Action) obj;
        return message.equals(a.message) && category.equals(a.category);
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