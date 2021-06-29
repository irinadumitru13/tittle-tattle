package com.example.tittle_tattle.ui.homeScreen.fragments.notifications;

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
import com.example.tittle_tattle.databinding.FragmentNotificationsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotificationsFragment extends Fragment {
    private FragmentNotificationsBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (!DisseminationService.active) {
            root.findViewById(R.id.notifRecView).setVisibility(View.INVISIBLE);
            root.findViewById(R.id.emptyNotifRec).setVisibility(View.VISIBLE);

            root.findViewById(R.id.textViewNoNotifications).setVisibility(View.INVISIBLE);

            Button btn = root.findViewById(R.id.btn_permissions_notifications);
            btn.setPaintFlags(btn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            btn.setOnClickListener(v -> {
                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                new Handler().postDelayed(() -> Navigation.findNavController(v).navigate(R.id.action_navigation_notifications_to_navigation_home), 500);
            });
        } else {
            List<MessageObject> notifications = DisseminationService.getNotifications();

            if (notifications.size() == 0) {
                root.findViewById(R.id.notifRecView).setVisibility(View.INVISIBLE);
                root.findViewById(R.id.emptyNotifRec).setVisibility(View.VISIBLE);

                root.findViewById(R.id.btn_permissions_notifications).setVisibility(View.INVISIBLE);
                root.findViewById(R.id.textViewPermissionsNotif).setVisibility(View.INVISIBLE);
                root.findViewById(R.id.textViewNoPermissionsNotif2).setVisibility(View.INVISIBLE);
            } else {
                root.findViewById(R.id.emptyNotifRec).setVisibility(View.INVISIBLE);

                RecyclerView recyclerView = root.findViewById(R.id.notifRecView);
                recyclerView.setVisibility(View.VISIBLE);

                final NotificationRecyclerViewAdapter adapter = new NotificationRecyclerViewAdapter(notifications);
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