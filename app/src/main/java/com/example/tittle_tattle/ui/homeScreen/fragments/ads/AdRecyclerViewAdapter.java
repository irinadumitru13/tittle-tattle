package com.example.tittle_tattle.ui.homeScreen.fragments.ads;

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

public class AdRecyclerViewAdapter extends RecyclerView.Adapter<AdRecyclerViewAdapter.AdViewHolder> {
    private final List<MessageObject> ownMessages;

    public AdRecyclerViewAdapter(List<MessageObject> ownMessages) {
        this.ownMessages = ownMessages;
    }

    public static class AdViewHolder extends RecyclerView.ViewHolder {
        View containerView;
        TextView textDate;
        TextView textContent;

        public AdViewHolder(View view) {
            super(view);
            containerView = view;
            textDate = view.findViewById(R.id.textDate);
            textContent = view.findViewById(R.id.textContent);
        }
    }

    @NonNull
    @NotNull
    @Override
    public AdRecyclerViewAdapter.AdViewHolder onCreateViewHolder(
            @NonNull @NotNull ViewGroup parent,
            int viewType) {
        return onCreateAdViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdRecyclerViewAdapter.AdViewHolder holder,
                                 int position) {
        MessageObject messageObject = ownMessages.get(position);
        onBindAdViewHolder(holder, messageObject);
    }

    @Override
    public int getItemCount() {
        return ownMessages.size();
    }

    public AdRecyclerViewAdapter.AdViewHolder onCreateAdViewHolder(@NotNull ViewGroup parent) {
        return new AdRecyclerViewAdapter.AdViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.ad_row, parent, false));
    }

    public void onBindAdViewHolder(@NotNull AdRecyclerViewAdapter.AdViewHolder holder,
                                             @NotNull MessageObject messageObject) {
        holder.textDate.setText(new Date(messageObject.getTimestamp()).toString());
        holder.textContent.setText(messageObject.getContent());
    }
}
