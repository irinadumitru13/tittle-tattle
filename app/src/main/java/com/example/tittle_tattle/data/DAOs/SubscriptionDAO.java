package com.example.tittle_tattle.data.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tittle_tattle.data.models.Subscription;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface SubscriptionDAO {
    @Insert()
    void insert(Subscription subscription);

    @Delete
    void delete(Subscription subscription);

    @Query("SELECT * FROM subscriptions WHERE user_id = :user_id")
    Single<List<Subscription>> findAllByUserId(String user_id);
}
