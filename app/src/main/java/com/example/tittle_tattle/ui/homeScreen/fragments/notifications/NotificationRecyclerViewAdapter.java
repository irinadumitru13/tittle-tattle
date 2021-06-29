package com.example.tittle_tattle.ui.homeScreen.fragments.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.data.models.MessageObject;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public class NotificationRecyclerViewAdapter
        extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.NotificationViewHolder> {
    private final List<MessageObject> notifications;

    public NotificationRecyclerViewAdapter(List<MessageObject> notifications) {
        this.notifications = notifications;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        View containerView;
        TextView textDate;
        TextView textContent;

        public NotificationViewHolder(View view) {
            super(view);
            containerView = view;
            textDate = view.findViewById(R.id.textDateNotif);
            textContent = view.findViewById(R.id.textContentNotif);
        }
    }

    @NonNull
    @NotNull
    @Override
    public NotificationRecyclerViewAdapter.NotificationViewHolder onCreateViewHolder(
            @NonNull @NotNull ViewGroup parent,
            int viewType) {
        return onCreateNotificationViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotificationRecyclerViewAdapter.NotificationViewHolder holder,
                                 int position) {
        MessageObject messageObject = notifications.get(position);
        onBindNotificationViewHolder(holder, messageObject);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public NotificationRecyclerViewAdapter.NotificationViewHolder onCreateNotificationViewHolder(@NotNull ViewGroup parent) {
        return new NotificationRecyclerViewAdapter.NotificationViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.notification_row, parent, false));
    }

    public void onBindNotificationViewHolder(@NotNull NotificationRecyclerViewAdapter.NotificationViewHolder holder,
                                             @NotNull MessageObject messageObject) {
        holder.textDate.setText(new Date(messageObject.getTimestamp()).toString());
        holder.textContent.setText(messageObject.getContent());
    }
}
