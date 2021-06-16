package com.example.tittle_tattle.data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tittle_tattle.data.models.Message;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface MessageDAO {
    @Insert()
    void insert(Message message);

    @Query("DELETE FROM messages WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM messages WHERE source = :user_id")
    Single<List<Message>> findAllByUserId(String user_id);

    @Query("SELECT * FROM messages")
    Single<List<Message>> findAll();
}
