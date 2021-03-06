package com.example.tittle_tattle.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "subscriptions",
        foreignKeys = {
            @ForeignKey(
                entity = User.class,
                parentColumns = "user_id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE
        )}
)
public class Subscription {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private final int subscription_id;

    @NonNull
    private final String name;

    private final int category_id;

    @NonNull
    private final Long user_id;

    public Subscription(int subscription_id, @NonNull String name, int category_id, @NonNull Long user_id) {
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
    public Long getUser_id() {
        return user_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
