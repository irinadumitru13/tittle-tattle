package com.example.tittle_tattle.data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tittle_tattle.data.models.MessageObject;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface MessageDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MessageObject message);

    @Query("DELETE FROM messages WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM messages WHERE source = :user_id ORDER BY timestamp")
    Single<List<MessageObject>> findAllByUserId(long user_id);

    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    List<MessageObject> findAll();
}
