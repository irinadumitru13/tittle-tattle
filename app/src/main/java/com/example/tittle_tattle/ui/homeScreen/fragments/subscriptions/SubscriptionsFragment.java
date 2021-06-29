package com.example.tittle_tattle.ui.homeScreen.fragments.subscriptions;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.algorithm.ISUser;
import com.example.tittle_tattle.data.models.Subscription;
import com.example.tittle_tattle.databinding.FragmentSubscriptionsBinding;
import com.example.tittle_tattle.ui.homeScreen.fragments.subscriptions.SubscriptionRecyclerViewAdapter;

import java.util.List;

public class SubscriptionsFragment extends Fragment {
    private FragmentSubscriptionsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.subscriptionsRecView);
        ConstraintLayout layout = root.findViewById(R.id.emptyRec);

        Button btn = root.findViewById(R.id.btn_top);
        btn.setPaintFlags(btn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_subscriptions_to_topics));

        List<Subscription> subscriptions = ISUser.getUser().getSubscriptions();
        final SubscriptionRecyclerViewAdapter adapter = new SubscriptionRecyclerViewAdapter(subscriptions, layout);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (subscriptions.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}