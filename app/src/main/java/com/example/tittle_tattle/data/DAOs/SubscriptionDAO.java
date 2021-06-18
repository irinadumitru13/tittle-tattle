package com.example.tittle_tattle.data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tittle_tattle.data.models.Subscription;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface SubscriptionDAO {
    @Insert()
    void insert(Subscription subscription);

    @Query("DELETE FROM subscriptions WHERE subscription_id = :subscription_id AND user_id = :user_id")
    void delete(int subscription_id, long user_id);

    @Query("SELECT * FROM subscriptions WHERE user_id = :user_id")
    Single<List<Subscription>> findAllByUserId(long user_id);
}
