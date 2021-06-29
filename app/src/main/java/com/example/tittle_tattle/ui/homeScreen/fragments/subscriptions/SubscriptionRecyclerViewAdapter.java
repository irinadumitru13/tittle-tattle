package com.example.tittle_tattle.ui.homeScreen.fragments.subscriptions;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.algorithm.ISUser;
import com.example.tittle_tattle.data.AppDatabase;
import com.example.tittle_tattle.data.models.Subscription;
import com.example.tittle_tattle.ui.homeScreen.fragments.topics.models.Subcategory;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SubscriptionRecyclerViewAdapter
        extends RecyclerView.Adapter<SubscriptionRecyclerViewAdapter.SViewHolder> {
    private final ConstraintLayout layout;
    private final List<Subscription> subscriptionList;

    public SubscriptionRecyclerViewAdapter(List<Subscription> subscriptions, ConstraintLayout layout) {
        this.layout = layout;
        subscriptionList = subscriptions;
    }


    public static class SViewHolder extends RecyclerView.ViewHolder {
        View containerView;
        TextView textView;
        Button button;

        public SViewHolder(View view) {
            super(view);
            containerView = view;
            textView = view.findViewById(R.id.textSubscription);
            button = view.findViewById(R.id.btn_unsubscribe);
        }
    }

    @NonNull
    @NotNull
    @Override
    public SubscriptionRecyclerViewAdapter.SViewHolder onCreateViewHolder(
            @NonNull @NotNull ViewGroup parent,
            int viewType) {
        return onCreateSubcategoryViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SubscriptionRecyclerViewAdapter.SViewHolder holder,
                                 int position) {
        Subscription subscription = subscriptionList.get(position);
        onBindSubscriptionViewHolder(holder, subscription, position);
    }

    @Override
    public int getItemCount() {
        return subscriptionList.size();
    }

    public SubscriptionRecyclerViewAdapter.SViewHolder onCreateSubcategoryViewHolder(@NotNull ViewGroup parent) {
        return new SubscriptionRecyclerViewAdapter.SViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.subscription_row, parent, false));
    }

    public void onBindSubscriptionViewHolder(@NotNull SubscriptionRecyclerViewAdapter.SViewHolder sViewHolder,
                                            @NotNull Subscription subscription,
                                            int position) {
        sViewHolder.textView.setText(subscription.getName());
        sViewHolder.button.setPaintFlags(
                sViewHolder.button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        sViewHolder.button.setOnClickListener(view -> {

                ISUser.getUser().unsubscribe(
                        new Subcategory(subscription.getName(),
                                        subscription.getSubscription_id(),
                                        subscription.getCategory_id()));
                removeAt(position);

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("[DB]", "unsubscribe");
                        AppDatabase.getInstance(view.getContext()).unsubscribe(subscription.getSubscription_id(), subscription.getUser_id());
                    }
                });
        });
    }

    private void removeAt(int position) {
        subscriptionList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, subscriptionList.size());

        if (subscriptionList.isEmpty()) {
            layout.setVisibility(View.VISIBLE);
        }
    }
}
