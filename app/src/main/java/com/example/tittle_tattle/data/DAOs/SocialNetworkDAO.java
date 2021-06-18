package com.example.tittle_tattle.data.DAOs;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tittle_tattle.algorithm.proto.SocialNetwork;
import com.example.tittle_tattle.data.models.SocialNetworkObject;

import java.util.List;

public interface SocialNetworkDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<SocialNetworkObject> socialNetwork);

    @Query("SELECT * FROM social_network")
    List<SocialNetwork> findAll();
}
