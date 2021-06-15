package com.example.tittle_tattle.data.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.example.tittle_tattle.data.models.Subscription;
import com.example.tittle_tattle.data.models.User;

@Dao
public interface SubscriptionDAO {
    @Insert()
    void insert(Subscription subscription);

    @Delete
    void delete(Subscription subscription);
}
