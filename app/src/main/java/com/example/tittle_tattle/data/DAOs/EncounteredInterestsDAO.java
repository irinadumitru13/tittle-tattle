package com.example.tittle_tattle.data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tittle_tattle.data.models.EncounteredInterestsObject;

import java.util.List;

@Dao
public interface EncounteredInterestsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<EncounteredInterestsObject> encounteredInterests);

    @Query("SELECT * FROM encountered_interests")
    List<EncounteredInterestsObject> findAll();
}
