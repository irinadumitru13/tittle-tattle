package com.example.tittle_tattle.data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tittle_tattle.data.models.User;

@Dao
public interface UserDAO {

    @Insert()
    void insert(User user);

    @Query("SELECT * FROM users WHERE user_id = :user_id")
    User findUserById(String user_id);
}
