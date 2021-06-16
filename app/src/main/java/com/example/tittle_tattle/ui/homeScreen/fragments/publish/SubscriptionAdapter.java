package com.example.tittle_tattle.ui.homeScreen.fragments.publish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tittle_tattle.R;
import com.example.tittle_tattle.data.models.Subscription;

import java.util.List;

public class SubscriptionAdapter extends ArrayAdapter<Subscription> {
    private Context context;
    private List<Subscription> subscriptions;

    public SubscriptionAdapter(@NonNull Context context, List<Subscription> subscriptions) {
        super(context, R.layout.checked_multiple_choice_item, subscriptions);
        this.context = context;
        this.subscriptions = subscriptions;
    }

    @Override
    public View getView(int position, @Nullable View view, ViewGroup parent) {
        View listItem = view;

        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.checked_multiple_choice_item, parent, false);
        }

        CheckedTextView textView = listItem.findViewById(R.id.checked_item);
        textView.setText(subscriptions.get(position).getName());

        return listItem;
    }



}
