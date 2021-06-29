package com.example.tittle_tattle.ui.homeScreen.fragments.topics;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.algorithm.DisseminationService;
import com.example.tittle_tattle.databinding.FragmentTopicsBinding;
import com.example.tittle_tattle.ui.homeScreen.fragments.topics.adapters.CategoryRecyclerViewAdapter;
import com.example.tittle_tattle.ui.homeScreen.fragments.topics.models.Category;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TopicsFragment extends Fragment {
    private FragmentTopicsBinding binding;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // start service for dissemination
                if (!DisseminationService.active) {
                    Intent intentService = new Intent(getActivity(), DisseminationService.class);
                    requireActivity().startForegroundService(intentService);
                }
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                Log.i("[PERMISSION]", "Denied");
            }});

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = FragmentTopicsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.topicsRecView);
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestPermission();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // start service for dissemination
            if (!DisseminationService.active) {
                Intent intentService = new Intent(getActivity(), DisseminationService.class);
                requireActivity().startForegroundService(intentService);
            }
//        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
//            showInContextUI(...);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            }
        }
    }
}