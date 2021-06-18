package com.example.tittle_tattle.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Entity(tableName = "social_network")
public class SocialNetworkObject {
    @PrimaryKey
    @NonNull
    private final Long id;

    @TypeConverters(SetToStringConverter.class)
    private Set<Integer> interests;

    public SocialNetworkObject(@NotNull Long id, Set<Integer> interests) {
        this.id = id;
        this.interests = interests;
    }

    public Set<Integer> getInterests() {
        return interests;
    }

    public void setInterests(Set<Integer> interests) {
        this.interests = interests;
    }

    @NonNull
    public Long getId() {
        return id;
    }
}
