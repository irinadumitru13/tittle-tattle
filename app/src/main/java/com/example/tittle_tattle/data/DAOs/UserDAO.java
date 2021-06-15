package com.example.tittle_tattle.data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.tittle_tattle.data.models.User;
import com.example.tittle_tattle.data.models.UserWithSubscriptions;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface UserDAO {

    @Insert()
    void insert(User user);

    @Query("SELECT * FROM users WHERE user_id = :user_id")
    User findUserById(String user_id);

    @Transaction
    @Query("SELECT * FROM users WHERE user_id = :user_id")
    Flowable<List<UserWithSubscriptions>> getUserWithSubscriptions(String user_id);
}
