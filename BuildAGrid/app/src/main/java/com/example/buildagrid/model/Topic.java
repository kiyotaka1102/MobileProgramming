package com.example.buildagrid.model;

public class Topic {
    private final int titleResId;
    private final int itemCount;
    private final int imageResId;

    public Topic(int titleResId, int itemCount, int imageResId) {
        this.titleResId = titleResId;
        this.itemCount = itemCount;
        this.imageResId = imageResId;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public int getItemCount() {
        return itemCount;
    }

    public int getImageResId() {
        return imageResId;
    }
}