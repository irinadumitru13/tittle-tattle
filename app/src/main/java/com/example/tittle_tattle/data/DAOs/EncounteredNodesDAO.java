package com.example.tittle_tattle.data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tittle_tattle.data.models.EncounteredNodesObject;

import java.util.List;

@Dao
public interface EncounteredNodesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EncounteredNodesObject> encounteredNodes);

    @Query("SELECT * FROM encountered_nodes")
    List<EncounteredNodesObject> findAll();
}
