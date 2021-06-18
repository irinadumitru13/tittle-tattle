package com.example.tittle_tattle.data.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    private final Long user_id;

    @NonNull
    private final String full_name;

    public User(@NonNull Long user_id, @NonNull String full_name) {
        this.user_id = user_id;
        this.full_name = full_name;
    }

    @NonNull
    public Long getUser_id() {
        return user_id;
    }

    @NonNull
    public String getFull_name() {
        return full_name;
    }
}
