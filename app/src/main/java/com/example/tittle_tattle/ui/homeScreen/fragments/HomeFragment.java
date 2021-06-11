package com.example.tittle_tattle.ui.homeScreen.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.tittle_tattle.R;
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
        sharedViewModel.getFullName().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button my_subs_btn = view.findViewById(R.id.btn_interests);
        my_subs_btn.setPaintFlags(my_subs_btn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        my_subs_btn.setOnClickListener(v -> {
//            SubscriptionsFragment subscriptionsFragment = new SubscriptionsFragment();
//
//            requireActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.nav_host_fragment_container, subscriptionsFragment, "subscriptions")
//                    .addToBackStack(null)
//                    .commit();
            Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_subscriptions);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}