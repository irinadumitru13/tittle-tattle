package com.example.tittle_tattle.data.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithSubscriptions {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "user_id",
            entityColumn = "user_id",
            entity = Subscription.class
    )
    public List<Subscription> subscriptions;

    public UserWithSubscriptions(User user, List<Subscription> subscriptions) {
        this.user = user;
        this.subscriptions = subscriptions;
    }
}
