package com.example.tittle_tattle.ui.homeScreen.fragments.topicsRecycler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.ui.homeScreen.fragments.topicsRecycler.models.Category;
import com.example.tittle_tattle.ui.homeScreen.fragments.topicsRecycler.models.Subcategory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecyclerViewAdapter
        extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.CViewHolder> {
    // elements to be expanded
    private final List<Category> categoryList;
    // maintain expansion state over entire listing - if fully-expanded, then true
    // maintain adapter attachment status to a recycler view - if attached, then true
    // reference to the recycler view that this adapter is currently attached to
    private RecyclerView parentRecyclerView = null;

    public CategoryRecyclerViewAdapter(List<Category> categories) {
        categoryList = categories;
    }

    public static class CViewHolder extends RecyclerView.ViewHolder {
        public View containerView;
        public TextView textView;
        public RecyclerView recyclerView;

        public CViewHolder(View view) {
            super(view);
            containerView = view;
            textView = view.findViewById(R.id.textCategory);
            recyclerView = view.findViewById(R.id.catRecView);
        }
    }

    @NonNull
    @NotNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return onCreateCategoryView(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CViewHolder holder, int position) {
        setUpSubcategoryRecyclerView(holder, position);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NotNull RecyclerView recyclerView) {
        parentRecyclerView = recyclerView;
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    @Override
    public void onDetachedFromRecyclerView(@NotNull RecyclerView recyclerView) {
        parentRecyclerView = null;
    }

    @NotNull
    private CViewHolder onCreateCategoryView(ViewGroup category) {
        CViewHolder cViewHolder = onCreateCategoryViewHolder(category);

        initializeSubcategoryRecyclerView(cViewHolder.recyclerView);

        cViewHolder.containerView.setOnClickListener(view -> {
            int position = cViewHolder.getAdapterPosition();
            Category expandable = categoryList.get(position);

            handleExpansion(expandable, position);
            handleLastPositionScroll(position);
        });

        return cViewHolder;
    }

    @NotNull
    private CViewHolder onCreateCategoryViewHolder(@NotNull ViewGroup category) {
        return new CViewHolder(LayoutInflater.from(category.getContext()).inflate(R.layout.category_row, category, false));
    }

    private void initializeSubcategoryRecyclerView(RecyclerView subcategoryRecyclerView) {
        if (subcategoryRecyclerView != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(subcategoryRecyclerView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

            subcategoryRecyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    private void reverseExpandableState(@NotNull Category category) {
        category.setExpanded(!category.isExpanded());
    }

    private void handleExpansion(Category category, int position) {
        reverseExpandableState(category);
        notifyItemChanged(position);
    }

    private void handleLastPositionScroll(int position) {
        if (position == categoryList.size() - 1)
            parentRecyclerView.smoothScrollToPosition(position);
    }

    private void setUpSubcategoryRecyclerView(@NotNull CViewHolder cViewHolder, int position) {
        Category category = categoryList.get(position);

        // make list of subcategories
        ArrayList<Subcategory> subcategories = new ArrayList<>();

        int subNamesResId = cViewHolder.containerView.getResources().getIdentifier(
                category.getResourceName(), "array", cViewHolder.containerView.getContext().getPackageName());
        int subIdResId = cViewHolder.containerView.getResources().getIdentifier(
                category.getResourceName() + "_id", "array", cViewHolder.containerView.getContext().getPackageName());

        String[] nameSubcategories = cViewHolder.containerView.getResources().getStringArray(subNamesResId);
        int[] idSubcategories = cViewHolder.containerView.getResources().getIntArray(subIdResId);

        for (int i = 0; i < nameSubcategories.length; i++) {
            subcategories.add(new Subcategory(nameSubcategories[i], idSubcategories[i], category.getCategoryId()));
        }


        SubcategoryRecyclerViewAdapter subcategoryRecyclerViewAdapter = new SubcategoryRecyclerViewAdapter(subcategories);
        cViewHolder.recyclerView.setAdapter(subcategoryRecyclerViewAdapter);

        clickEvent(category, cViewHolder.recyclerView);
        onBindParentViewHolder(cViewHolder, category);
    }

    private void clickEvent(@NotNull Category category, RecyclerView recyclerView) {
        if (category.isExpanded())
            recyclerView.setVisibility(View.VISIBLE);
        else
            recyclerView.setVisibility(View.GONE);
    }

    private void onBindParentViewHolder(@NotNull CViewHolder cViewHolder, @NotNull Category category) {
        cViewHolder.textView.setText(category.getName());
    }
}
