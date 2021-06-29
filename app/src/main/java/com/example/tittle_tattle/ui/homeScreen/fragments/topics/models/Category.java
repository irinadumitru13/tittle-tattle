package com.example.tittle_tattle.ui.homeScreen.fragments.topics.models;

public class Category {
    private final String name;
    private final String resourceName;
    private final int categoryId;
    private boolean isExpanded;

    public Category(String name, String resourceName, int categoryId) {
        this.name = name;
        this.resourceName = resourceName;
        this.categoryId = categoryId;
        this.isExpanded = false;
    }

    public String getName() {
        return name;
    }

    public String getResourceName() {
        return resourceName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
