package com.example.tittle_tattle.ui.homeScreen.fragments.topicsRecycler.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.ui.homeScreen.fragments.topicsRecycler.models.Subcategory;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SubcategoryRecyclerViewAdapter extends RecyclerView.Adapter<SubcategoryRecyclerViewAdapter.SViewHolder> {
    // elements to be expanded
    private final List<Subcategory> subcategoryList;

    public SubcategoryRecyclerViewAdapter(List<Subcategory> subcategories) {
        subcategoryList = subcategories;
    }

    public static class SViewHolder extends RecyclerView.ViewHolder {
        View containerView;
        TextView textView;
        Button button;

        public SViewHolder(View view) {
            super(view);
            containerView = view;
            textView = view.findViewById(R.id.textSubcategory);
            button = view.findViewById(R.id.btn_subscribe);
        }
    }

    @NonNull
    @NotNull
    @Override
    public SViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return onCreateSubcategoryViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SViewHolder holder, int position) {
        Subcategory subcategory = subcategoryList.get(position);
        onBindSubcategoryViewHolder(holder, subcategory);
    }

    @Override
    public int getItemCount() {
        return subcategoryList.size();
    }

    public SViewHolder onCreateSubcategoryViewHolder(ViewGroup parent) {
        return new SViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_row, parent, false));
    }

    public void onBindSubcategoryViewHolder(SViewHolder sViewHolder, Subcategory subcategory) {
        sViewHolder.textView.setText(subcategory.getName());

        sViewHolder.button.setOnClickListener(view -> {
            Log.i("[BUTTON]", "Pressed " + subcategory.getName() + ", " + subcategory.getSubcategoryId());
        });
    }
}
