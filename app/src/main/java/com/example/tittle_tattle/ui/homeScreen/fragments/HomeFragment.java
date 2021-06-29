package com.example.tittle_tattle.ui.homeScreen.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.algorithm.DisseminationService;
import com.example.tittle_tattle.databinding.FragmentHomeBinding;
import com.example.tittle_tattle.ui.homeScreen.SharedViewModel;

public class HomeFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.nameText;
        sharedViewModel.getFullNameLive().observe(getViewLifecycleOwner(), textView::setText);

        Button my_subs_btn = root.findViewById(R.id.btn_interests);
        my_subs_btn.setPaintFlags(my_subs_btn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        my_subs_btn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_subscriptions));

        Button topics_btn = root.findViewById(R.id.btn_topics);
        topics_btn.setPaintFlags(topics_btn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        topics_btn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_topics));

        Button messages_btn = root.findViewById(R.id.btn_pub_msg);
        messages_btn.setPaintFlags(topics_btn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        messages_btn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_adsFragment));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermissions();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void checkPermissions() {
        String[] permissions;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            permissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            };
        } else {
            permissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
        }

        boolean granted = true;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                granted = false;
            }
        }

        if (granted && !DisseminationService.active) {
            Intent intentService = new Intent(getContext(), DisseminationService.class);
            requireActivity().startForegroundService(intentService);
        }

    }
}