package com.example.tittle_tattle.ui.homeScreen.fragments.ads;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tittle_tattle.BuildConfig;
import com.example.tittle_tattle.R;
import com.example.tittle_tattle.algorithm.DisseminationService;
import com.example.tittle_tattle.data.models.MessageObject;
import com.example.tittle_tattle.databinding.FragmentAdsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdsFragment extends Fragment {
    private FragmentAdsBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (!DisseminationService.active) {
            root.findViewById(R.id.adsRecView).setVisibility(View.INVISIBLE);
            root.findViewById(R.id.emptyAdsRec).setVisibility(View.VISIBLE);

            root.findViewById(R.id.btn_publish_ads).setVisibility(View.INVISIBLE);
            root.findViewById(R.id.textViewNoAds).setVisibility(View.INVISIBLE);

            Button btn = root.findViewById(R.id.btn_permissions);
            btn.setPaintFlags(btn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            btn.setOnClickListener(v -> {
                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                new Handler().postDelayed(() -> Navigation.findNavController(v).navigate(R.id.action_adsFragment_to_navigation_home), 500);
            });
        } else {
            List<MessageObject> ownMessages = DisseminationService.getOwnMessages();

            if (ownMessages.size() == 0) {
                root.findViewById(R.id.adsRecView).setVisibility(View.INVISIBLE);
                root.findViewById(R.id.emptyAdsRec).setVisibility(View.VISIBLE);

                root.findViewById(R.id.btn_permissions).setVisibility(View.INVISIBLE);
                root.findViewById(R.id.textViewPermissionsAds).setVisibility(View.INVISIBLE);
                root.findViewById(R.id.textViewNoPermissionsAds2).setVisibility(View.INVISIBLE);

                Button btn = root.findViewById(R.id.btn_publish_ads);
                btn.setPaintFlags(btn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                btn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_adsFragment_to_navigation_publish));
            } else {
                root.findViewById(R.id.emptyAdsRec).setVisibility(View.INVISIBLE);

                RecyclerView recyclerView = root.findViewById(R.id.adsRecView);
                recyclerView.setVisibility(View.VISIBLE);

                final AdRecyclerViewAdapter adapter = new AdRecyclerViewAdapter(ownMessages);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}