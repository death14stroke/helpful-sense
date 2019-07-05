package com.andruid.magic.helpfulsense.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    private final String name;
    private final int icon, iconColor;

    public Category(String name, int icon, int iconColor) {
        this.name = name;
        this.icon = icon;
        this.iconColor = iconColor;
    }

    private Category(Parcel in) {
        name = in.readString();
        icon = in.readInt();
        iconColor = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public int getIconColor() {
        return iconColor;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || obj.getClass() != getClass())
            return false;
        Category c = (Category) obj;
        return name.equals(c.name) && icon == c.icon && iconColor == c.iconColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(icon);
        dest.writeInt(iconColor);
    }
}