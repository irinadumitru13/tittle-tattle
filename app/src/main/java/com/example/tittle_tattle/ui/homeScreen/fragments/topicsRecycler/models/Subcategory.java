package com.example.tittle_tattle.ui.homeScreen.fragments.topicsRecycler.models;

public class Subcategory {
    private final String name;
    private final int subcategoryId;
    private final int categoryId;

    public Subcategory(String name, int subcategoryId, int categoryId) {
        this.name = name;
        this.subcategoryId = subcategoryId;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public int getSubcategoryId() {
        return subcategoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
