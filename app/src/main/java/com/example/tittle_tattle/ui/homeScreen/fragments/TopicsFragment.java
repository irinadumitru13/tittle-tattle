package com.example.tittle_tattle.ui.homeScreen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.databinding.FragmentTopicsBinding;
import com.example.tittle_tattle.ui.homeScreen.fragments.topicsRecycler.adapters.CategoryRecyclerViewAdapter;
import com.example.tittle_tattle.ui.homeScreen.fragments.topicsRecycler.models.Category;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TopicsFragment extends Fragment {
    private FragmentTopicsBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = FragmentTopicsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.topicsRecView);
        ArrayList<Category> categories = new ArrayList<>();

        String[] nameResCategories = getResources().getStringArray(R.array.categories_resources);
        int[] idCategories = getResources().getIntArray(R.array.categories_resources_id);
        int id;

        for (int i = 0; i < nameResCategories.length; i++) {
            id = getResources().getIdentifier(nameResCategories[i], "string", requireActivity().getPackageName());
            categories.add(new Category(getResources().getString(id), nameResCategories[i], idCategories[i]));
        }

        final CategoryRecyclerViewAdapter adapter = new CategoryRecyclerViewAdapter(categories);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}