package com.example.tittle_tattle.data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tittle_tattle.data.models.SocialNetworkObject;

import java.util.List;

@Dao
public interface SocialNetworkDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SocialNetworkObject> socialNetwork);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SocialNetworkObject friend);

    @Query("SELECT * FROM social_network")
    List<SocialNetworkObject> findAll();
}
