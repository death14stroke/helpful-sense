package com.andruid.magic.helpfulsense.model;

public class Category {
    private final String name;
    private final int icon;

    public Category(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }
}