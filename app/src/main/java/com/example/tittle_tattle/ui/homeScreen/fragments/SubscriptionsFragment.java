package com.example.tittle_tattle.ui.homeScreen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tittle_tattle.databinding.FragmentSubscriptionsBinding;
import com.example.tittle_tattle.ui.homeScreen.SharedViewModel;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SubscriptionsFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private FragmentSubscriptionsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.nameText;
//        sharedViewModel.getFullName().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}