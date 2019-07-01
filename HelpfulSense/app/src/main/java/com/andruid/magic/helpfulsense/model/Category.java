package com.andruid.magic.helpfulsense.model;

public class Category {
    private final String name;
    private final int icon, bgColor;

    public Category(String name, int icon, int bgColor) {
        this.name = name;
        this.icon = icon;
        this.bgColor = bgColor;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public int getBgColor() {
        return bgColor;
    }
}