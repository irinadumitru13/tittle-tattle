package com.example.tittle_tattle.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "subscriptions")
public class Subscription {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private int subscription_id;

    @NonNull
    private String name;

    @NonNull
    private int category_id;

    @NonNull
    private String user_id;

    public Subscription(int subscription_id, @NonNull String name, int category_id, @NonNull String user_id) {
        this.subscription_id = subscription_id;
        this.name = name;
        this.category_id = category_id;
        this.user_id = user_id;
    }

    public int getSubscription_id() {
        return subscription_id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getCategory_id() {
        return category_id;
    }

    @NonNull
    public String getUser_id() {
        return user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
