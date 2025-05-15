package com.example.food_selling_app.model;

public class AdminOption {
    private final String title;
    private final int iconResId;

    public AdminOption(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }
}
