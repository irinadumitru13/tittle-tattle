package com.example.tittle_tattle.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "encountered_interests")
public class EncounteredInterestsObject {
    @PrimaryKey
    @NonNull
    private final Integer id;

    @NonNull
    private final Long times;

    public EncounteredInterestsObject(@NotNull Integer id, @NotNull Long times) {
        this.id = id;
        this.times = times;
    }

    @NotNull
    public Integer getId() {
        return id;
    }

    @NonNull
    public Long getTimes() {
        return times;
    }
}
